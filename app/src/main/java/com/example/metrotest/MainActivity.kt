package com.example.metrotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.FlowRow
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(linea.estaciones) { estacion ->
                EstacionCard(estacion = estacion)
            }
        }
    }
}

@Composable
fun EstacionCard(estacion: Estacion) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (estacion.tiene_incidencias)
                Color(0xFFFFCDD2) else Color.White
        )
    ) {
        Column {
            // Header - siempre visible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
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
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Contraer" else "Expandir"
                )
            }

            // Contenido expandible
            if (expanded) {
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
                        
                        // FlowRow para mostrar las imágenes de las correspondencias
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = 5,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            estacion.correspondencias.forEach { svgUrl ->
                                Card(
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(svgUrl)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Correspondencia de línea",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(6.dp)
                                    )
                                }
                            }
                        }
                    }

                   /* // Incidencias generales
                    if (estacion.incidencias_generales.isNotBlank()) {
                        Text(
                            text = "Incidencias:",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color(0xFFD32F2F)
                        )
                        Text(
                            text = estacion.incidencias_generales,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }*/

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
                                Text(
                                    text = incidencia.tipo,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = Color(0xFFE65100)
                                )
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

