package com.example.metrotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.input.TextFieldValue
import java.io.IOException
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MetroMadridTheme {
                MetroApp()
            }
        }
    }
}

@Serializable
data class MetroData(
    val lineas: List<Linea>
)

@Serializable
data class Linea(
    val nombre: String,
    val estaciones: List<Estacion>
)

@Serializable
data class Estacion(
    val nombre: String,
    val incidencias_generales: String,
    val correspondencias: List<String>,
    val incidencias_especificas: List<IncidenciaEspecifica>,
    val tiene_incidencias: Boolean
)

@Serializable
data class IncidenciaEspecifica(
    val tipo: String,
    val imagen: String,
    val descripcion: String
)


@Composable
fun MetroMadridTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF1976D2),
            secondary = Color(0xFF03DAC6)
        ),
        content = content
    )
}

@Composable
fun MetroApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Cargar datos del JSON
    val metroData = remember {
        try {
            val jsonString =
                context.assets.open("metro_madrid.json").bufferedReader().use { it.readText() }
            Json.decodeFromString<MetroData>(jsonString)
        } catch (e: IOException) {
            // Datos de ejemplo para preview
            MetroData(
                lineas = listOf(
                    Linea(
                        nombre = "linea-1",
                        estaciones = listOf(
                            Estacion(
                                nombre = "Pinar de Chamartín",
                                incidencias_generales = "",
                                correspondencias = emptyList(),
                                incidencias_especificas = emptyList(),
                                tiene_incidencias = false
                            ),
                            Estacion(
                                nombre = "Bambú",
                                incidencias_generales = "Escaleras mecánicas fuera de servicio",
                                correspondencias = emptyList(),
                                incidencias_especificas = listOf(
                                    IncidenciaEspecifica(
                                        tipo = "Escaleras mecánicas",
                                        imagen = "",
                                        descripcion = "Fuera de servicio por trabajos de mejora"
                                    )
                                ),
                                tiene_incidencias = true
                            )
                        )
                    )
                )
            )
        }
    }

    NavHost(
        navController = navController,
        startDestination = "lineas"
    ) {
        composable("lineas") {
            LineasScreen(
                lineas = metroData.lineas,
                onLineaClick = { linea ->
                    navController.navigate("estaciones/${linea.nombre}")
                }
            )
        }

        composable("estaciones/{lineaNombre}") { backStackEntry ->
            val lineaNombre = backStackEntry.arguments?.getString("lineaNombre")
            val linea = metroData.lineas.find { it.nombre == lineaNombre }

            if (linea != null) {
                EstacionesScreen(
                    linea = linea,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineasScreen(
    lineas: List<Linea>,
    onLineaClick: (Linea) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Metro Madrid") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(lineas) { linea ->
                LineaCard(
                    linea = linea,
                    onClick = { onLineaClick(linea) }
                )
            }
        }
    }
}

@Composable
fun LineaCard(
    linea: Linea,
    onClick: () -> Unit
) {
    val estacionesConIncidencias = linea.estaciones.count { it.tiene_incidencias }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (estacionesConIncidencias > 0)
                Color(0xFFFFEBEE) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = formatLineaName(linea.nombre),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${linea.estaciones.size} estaciones",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                if (estacionesConIncidencias > 0) {
                    Text(
                        text = "$estacionesConIncidencias con incidencias",
                        fontSize = 12.sp,
                        color = Color(0xFFD32F2F)
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ver estaciones",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstacionesScreen(
    linea: Linea,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var showOnlyIncidencias by remember { mutableStateOf(false) }

    val filteredEstaciones = remember(searchQuery.text, showOnlyIncidencias) {
        derivedStateOf {
            linea.estaciones.filter { estacion ->
                val matchesSearch = estacion.nombre.contains(searchQuery.text, ignoreCase = true)
                val matchesFilter = !showOnlyIncidencias || estacion.tiene_incidencias
                matchesSearch && matchesFilter
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(formatLineaName(linea.nombre)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search and Filter Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    placeholder = { Text("Buscar estación...") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mostrar solo estaciones con incidencias",
                        fontSize = 14.sp
                    )
                    Switch(
                        checked = showOnlyIncidencias,
                        onCheckedChange = { showOnlyIncidencias = it }
                    )
                }
            }

            // Stations List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredEstaciones.value) { estacion ->
                    EstacionCard(estacion = estacion,modifier = Modifier.animateItem())
                }
            }
        }
    }
}

@Composable
fun EstacionCard(estacion: Estacion, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val hasExpandableContent = estacion.correspondencias.isNotEmpty() || estacion.incidencias_especificas.isNotEmpty()

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (estacion.tiene_incidencias)
                Color(0xFFFFCDD2) else Color.White
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (hasExpandableContent) {
                            Modifier.clickable { expanded = !expanded }
                        } else {
                            Modifier
                        }
                    )
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (estacion.tiene_incidencias) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Incidencias",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = estacion.nombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    AnimatedVisibility(
                        visible = !expanded,
                        enter = expandIn(expandFrom = Alignment.Center),
                        exit = shrinkOut(shrinkTowards = Alignment.Center)
                    ) {
                   // if (!expanded) {
                        Row {
                            estacion.incidencias_especificas.map { it.imagen }.forEach {
                                val (resourceId, description) = getImageDrawableResource(it)
                                Image(
                                    painter = painterResource(resourceId),
                                    contentDescription = description,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(6.dp)
                                )
                            }
                        }
                    }
                }

                if (hasExpandableContent) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Contraer" else "Expandir"
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded && hasExpandableContent,
                enter = fadeIn(animationSpec = tween(300)) + expandVertically(
                    animationSpec = tween(300)
                ),
                exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(
                    animationSpec = tween(300)
                )
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))

                    // Correspondencias
                    if (estacion.correspondencias.isNotEmpty()) {
                        Text(
                            text = "Correspondencias:",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${estacion.correspondencias.size} líneas",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = 5,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            estacion.correspondencias.forEach { svgUrl ->
                                val (resourceId, description) = getImageDrawableResource(svgUrl)
                                Image(
                                    painter = painterResource(resourceId),
                                    contentDescription = description,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(6.dp)
                                )
                            }
                        }
                    }

                    // Incidencias específicas
                    estacion.incidencias_especificas.forEach { incidencia ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF3E0)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = incidencia.tipo,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = Color(0xFFE65100)
                                    )
                                    val (resourceId, description) = getImageDrawableResource(incidencia.imagen)
                                    Image(
                                        painter = painterResource(resourceId),
                                        contentDescription = description,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(6.dp)
                                    )
                                }

                                if (incidencia.descripcion.isNotBlank()) {
                                    Text(
                                        text = incidencia.descripcion,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun formatLineaName(nombre: String): String {
    return when {
        nombre.startsWith("linea-") -> "Línea ${nombre.removePrefix("linea-").uppercase()}"
        nombre.startsWith("ml") -> "Metro Ligero ${nombre.removePrefix("ml").uppercase()}"
        nombre == "ramal" -> "Ramal"
        else -> nombre.replaceFirstChar { it.uppercase() }
    }
}

fun getImageDrawableResource(svgUrl: String): Pair<Int, String> {
    // Extract filename from URL
    val fileName = svgUrl.substringAfterLast("/").removeSuffix(".svg")
    
    return when {
        fileName.startsWith("linea-") -> {
            val numero = fileName.removePrefix("linea-")
            when (numero) {
                "1" -> Pair(R.drawable.linea_1, "Línea 1")
                "2" -> Pair(R.drawable.linea_2, "Línea 2")
                "3" -> Pair(R.drawable.linea_3, "Línea 3")
                "4" -> Pair(R.drawable.linea_4, "Línea 4")
                "5" -> Pair(R.drawable.linea_5, "Línea 5")
                "6" -> Pair(R.drawable.linea_6_circular, "Línea 6 Circular")
                "7" -> Pair(R.drawable.linea_7, "Línea 7")
                "8" -> Pair(R.drawable.linea_8, "Línea 8")
                "9" -> Pair(R.drawable.linea_9, "Línea 9")
                "10" -> Pair(R.drawable.linea_10, "Línea 10")
                "11" -> Pair(R.drawable.linea_11, "Línea 11")
                "12" -> Pair(R.drawable.linea_12_metrosur, "Línea 12 MetroSur")
                else -> Pair(R.drawable.linea_1, "Línea 1") // Default fallback
            }
        }
        fileName.startsWith("ml") -> {
            val numero = fileName.removePrefix("ml")
            when (numero) {
                "1" -> Pair(R.drawable.ml1, "Metro Ligero 1")
                "2" -> Pair(R.drawable.ml2, "Metro Ligero 2")
                "3" -> Pair(R.drawable.ml3, "Metro Ligero 3")
                else -> Pair(R.drawable.ml1, "Metro Ligero 1") // Default fallback
            }
        }
        fileName == "ramal" -> Pair(R.drawable.ramal, "Ramal")
        fileName == "escaleras-mecanicas" -> Pair(R.drawable.escaleras_mecanicas, "Escaleras Mecánicas")
        fileName == "ascensores" -> Pair(R.drawable.ascensores, "Ascensores")
        fileName == "pasillos-rodantes" -> Pair(R.drawable.pasillos_rodantes, "Pasillos Rodantes")
        else -> Pair(R.drawable.linea_1, "Línea 1") // Default fallback
    }
}

