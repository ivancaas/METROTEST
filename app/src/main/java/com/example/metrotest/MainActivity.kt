package com.example.metrotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetroApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }

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

    Scaffold(
//        bottomBar = {
//            NavigationBar(
//                containerColor = Color.White
//            ) {
//                NavigationBarItem(
//                    icon = { Icon(Icons.Default.Home, contentDescription = "Líneas") },
//                    label = { Text("Líneas") },
//                    selected = selectedTab == 0,
//                    onClick = {
//                        selectedTab = 0
//                        navController.navigate("lineas") {
//                            popUpTo("lineas") { inclusive = true }
//                        }
//                    }
//                )
//                NavigationBarItem(
//                    icon = { Icon(Icons.Default.Map, contentDescription = "Mapa") },
//                    label = { Text("Mapa") },
//                    selected = selectedTab == 1,
//                    onClick = { selectedTab = 1 }
//                )
//                NavigationBarItem(
//                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
//                    label = { Text("Favoritos") },
//                    selected = selectedTab == 2,
//                    onClick = { selectedTab = 2 }
//                )
//                NavigationBarItem(
//                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Alertas") },
//                    label = { Text("Alertas") },
//                    selected = selectedTab == 3,
//                    onClick = { selectedTab = 3 }
//                )
//                NavigationBarItem(
//                    icon = { Icon(Icons.Default.Settings, contentDescription = "Más") },
//                    label = { Text("Más") },
//                    selected = selectedTab == 4,
//                    onClick = { selectedTab = 4 }
//                )
//            }
//        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "lineas",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("lineas") {
                selectedTab = 0
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineasScreen(
    lineas: List<Linea>,
    onLineaClick: (Linea) -> Unit
) {
    Scaffold(
        topBar = {
            // Custom TopBar con diseño mejorado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1976D2))
                    .statusBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "ESTADO DEL SERVICIO",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "Metro Madrid",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección de Líneas de Metro
            item {
                Text(
                    text = "Líneas de Metro",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp )
                )
            }
            
            // Lista de líneas
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
    val numeroLinea = getLineaAvatarText(linea.nombre)
    val colorLinea = getLineaColor(linea.nombre)
    
    // Terminales: primera y última estación
    val primeraEstacion = linea.estaciones.firstOrNull()?.nombre ?: ""
    val ultimaEstacion = linea.estaciones.lastOrNull()?.nombre ?: ""
    val terminales = if (primeraEstacion.isNotEmpty() && ultimaEstacion.isNotEmpty()) {
        "$primeraEstacion — $ultimaEstacion"
    } else {
        ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Icono de la línea
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = colorLinea,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = numeroLinea,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Información de la línea
                Column {
                    Text(
                        text = formatLineaName(linea.nombre),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    if (terminales.isNotEmpty()) {
                        Text(
                            text = terminales,
                            fontSize = 13.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    // Estado de la línea
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Badge de estado con background tipo cápsula
                        if (estacionesConIncidencias > 0) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFFD32F2F).copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Incidencias",
                                    tint = Color(0xFFD32F2F),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$estacionesConIncidencias INCIDENCIA${if (estacionesConIncidencias > 1) "S" else ""}",
                                    fontSize = 11.sp,
                                    color = Color(0xFFD32F2F),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Servicio Normal",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "SERVICIO NORMAL",
                                    fontSize = 11.sp,
                                    color = Color(0xFF4CAF50),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "${linea.estaciones.size} Estaciones",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            // Icono de flecha
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ver detalles",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// Función para obtener el texto del avatar de la línea
fun getLineaAvatarText(nombre: String): String {
    return when {
        nombre.startsWith("linea-") -> {
            // Extraer solo el número de la línea
            val partes = nombre.removePrefix("linea-").split("-")
            partes.firstOrNull()?.uppercase() ?: ""
        }
        nombre.startsWith("ml") -> nombre.uppercase()
        nombre == "ramal" -> "R"
        else -> nombre.take(2).uppercase()
    }
}

// Función para obtener el color de cada línea
fun getLineaColor(nombre: String): Color {
    return when {
        nombre.startsWith("linea-6") -> Color(0xFF808285)  // Gris (para linea-6 y linea-6-circular)
        nombre.startsWith("linea-12") -> Color(0xFFA49A00) // Amarillo oscuro (para linea-12 y linea-12-metrosur)
        nombre == "linea-1" -> Color(0xFF00A9E0)  // Celeste
        nombre == "linea-2" -> Color(0xFFED1C24)  // Rojo
        nombre == "linea-3" -> Color(0xFFFFC000)  // Amarillo
        nombre == "linea-4" -> Color(0xFF9B3888)  // Morado
        nombre == "linea-5" -> Color(0xFF7FBF3F)  // Verde claro
        nombre == "linea-7" -> Color(0xFFFF6600)  // Naranja
        nombre == "linea-8" -> Color(0xFFE5007E)  // Rosa
        nombre == "linea-9" -> Color(0xFF9B3888)  // Morado oscuro
        nombre == "linea-10" -> Color(0xFF003F87) // Azul oscuro
        nombre == "linea-11" -> Color(0xFF006633) // Verde oscuro
        nombre == "ramal" -> Color(0xFF1976D2)   // Azul
        else -> Color(0xFF1976D2)       // Azul por defecto
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstacionesScreen(
    linea: Linea,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showOnlyIncidencias by remember { mutableStateOf(false) }

    val filteredEstaciones = remember(searchQuery, showOnlyIncidencias) {
        derivedStateOf {
            linea.estaciones.filter { estacion ->
                val matchesSearch = estacion.nombre.contains(searchQuery, ignoreCase = true)
                val matchesFilter = !showOnlyIncidencias || estacion.tiene_incidencias
                matchesSearch && matchesFilter
            }
        }
    }
    
    val colorLinea = getLineaColor(linea.nombre)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = formatLineaName(linea.nombre),
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Info */ }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Información",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorLinea,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // Search and Filter Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(16.dp)
            ) {
                // Search field con icono
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = { 
                        Text(
                            text = "Buscar estación...",
                            color = Color.Gray
                        ) 
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color.Gray
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mostrar solo estaciones con incidencias",
                        fontSize = 14.sp,
                        color = Color(0xFF333333)
                    )
                    Switch(
                        checked = showOnlyIncidencias,
                        onCheckedChange = { showOnlyIncidencias = it }
                    )
                }
            }

            // Stations List con línea vertical
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                // Línea vertical de la línea de metro
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxSize()
                        .padding(start = 32.dp)
                        .background(colorLinea.copy(alpha = 0.3f))
                )
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        bottom = paddingValues.calculateBottomPadding()
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredEstaciones.value.size) { index ->
                        EstacionCard(
                            estacion = filteredEstaciones.value[index],
                            modifier = Modifier.animateItem(),
                            colorLinea = colorLinea
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EstacionCard(
    estacion: Estacion, 
    modifier: Modifier,
    colorLinea: Color
) {
    var expanded by remember { mutableStateOf(false) }
    val hasExpandableContent = estacion.correspondencias.isNotEmpty() || estacion.incidencias_especificas.isNotEmpty()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Punto de la estación en la línea vertical
        Box(
            modifier = Modifier
                .padding(top = 24.dp)
                .size(16.dp)
                .background(colorLinea, shape = RoundedCornerShape(50))
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Card de la estación
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (hasExpandableContent) {
                        Modifier.clickable { expanded = !expanded }
                    } else {
                        Modifier
                    }
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (estacion.tiene_incidencias)
                    Color(0xFFFEE9E7) else Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
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
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        
                        // Badges de correspondencias inline (cuando no está expandido)
                        if (!expanded && estacion.correspondencias.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                estacion.correspondencias.take(3).forEach { svgUrl ->
                                    CorrespondenciaBadge(svgUrl)
                                }
                            }
                        }
                    }

                    if (hasExpandableContent) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Contraer" else "Expandir",
                            tint = Color.Gray
                        )
                    }
                }

                // Contenido expandido
                AnimatedVisibility(
                    visible = expanded && hasExpandableContent,
                    enter = fadeIn(animationSpec = tween(200)) + expandVertically(
                        animationSpec = tween(200)
                    ),
                    exit = fadeOut(animationSpec = tween(200)) + shrinkVertically(
                        animationSpec = tween(200)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        // Correspondencias
                        if (estacion.correspondencias.isNotEmpty()) {
                            Text(
                                text = "CORRESPONDENCIAS",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                estacion.correspondencias.forEach { svgUrl ->
                                    CorrespondenciaBadge(svgUrl, isLarge = true)
                                }
                            }
                        }

                        // Incidencias específicas
                        estacion.incidencias_especificas.forEach { incidencia ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                ) {
                                    val (resourceId, description) = getImageDrawableResource(incidencia.imagen)
                                    Image(
                                        painter = painterResource(resourceId),
                                        contentDescription = description,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = incidencia.tipo,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color(0xFFFF6B00)
                                    )
                                }

                                if (incidencia.descripcion.isNotBlank()) {
                                    Text(
                                        text = incidencia.descripcion,
                                        fontSize = 14.sp,
                                        color = Color(0xFF666666),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }
                                
                                Text(
                                    text = "Fuera de servicio. Estamos analizando los motivos de la parada.",
                                    fontSize = 14.sp,
                                    color = Color(0xFFD32F2F),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Componente para badges de correspondencias
@Composable
fun CorrespondenciaBadge(svgUrl: String, isLarge: Boolean = false) {
    val lineaName = svgUrl.substringAfterLast("/").removeSuffix(".svg")
    val badgeColor = when {
        lineaName.startsWith("linea-") -> getLineaColor(lineaName)
        lineaName.startsWith("ml") -> Color(0xFF4CAF50)
        lineaName.contains("c1") || lineaName.contains("C1") -> Color(0xFF808080)
        lineaName.contains("c2") || lineaName.contains("C2") -> Color(0xFF808080)
        else -> Color(0xFF1976D2)
    }
    
    val badgeText = when {
        lineaName.startsWith("linea-") -> {
            // Extraer solo el número de la línea (sin sufijos como -circular o -metrosur)
            val partes = lineaName.removePrefix("linea-").split("-")
            partes.firstOrNull()?.uppercase() ?: ""
        }
        lineaName.contains("c1", ignoreCase = true) -> "C1"
        lineaName.contains("c2", ignoreCase = true) -> "C2"
        lineaName.contains("c3", ignoreCase = true) -> "C3"
        lineaName.contains("c4", ignoreCase = true) -> "C4"
        lineaName.startsWith("ml") -> lineaName.uppercase()
        lineaName == "ramal" -> "R"
        else -> lineaName.take(2).uppercase()
    }
    
    val size = if (isLarge) 36.dp else 28.dp
    val fontSize = if (isLarge) 14.sp else 11.sp
    
    Box(
        modifier = Modifier
            .size(size)
            .background(badgeColor, shape = RoundedCornerShape(50)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = badgeText,
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun formatLineaName(nombre: String): String {
    return when {
        nombre == "linea-6-circular" -> "Línea 6-CIRCULAR"
        nombre == "linea-12-metrosur" -> "Línea 12-METROSUR"
        nombre.startsWith("linea-") -> {
            val numero = nombre.removePrefix("linea-")
            "Línea ${numero.uppercase()}"
        }
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

