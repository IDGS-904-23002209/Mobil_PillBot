package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.pantallas.*
import com.jimenaoropeza.pillbot.presentation.components.Screen
import com.jimenaoropeza.pillbot.presentation.screens.Notificaciones
import com.jimenaoropeza.pillbot.viewmodel.MedicamentoViewModel
import com.jimenaoropeza.pillbot.viewmodel.RecordatorioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillBotNavigation(
    navController: NavHostController,
    usuarioIdInicial: Int,
    tomaHoyViewModel: TomaHoyViewModel = viewModel()
) {
    // =========================================================================
    // SOLUCIÓN AL ERROR (image_6d3137.jpg): Instanciar los ViewModels faltantes
    // =========================================================================
    val medicamentoViewModel: MedicamentoViewModel = viewModel()
    val recordatorioViewModel: RecordatorioViewModel = viewModel()

    // Monitorea en qué pantalla se encuentra el usuario actualmente
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Carga las tomas de manera global para alimentar el globo de notificaciones
    LaunchedEffect(usuarioIdInicial) {
        tomaHoyViewModel.cargarTomasHoy(usuarioId = usuarioIdInicial)
    }
    val tomasHoy by tomaHoyViewModel.tomasHoy
    val tomasPendientes = tomasHoy.count { !it.tomado }

    // Definición de las rutas correspondientes a la barra inferior
    val routes = listOf(
        Screen.Inicio.route,         // 0
        "formulario",                // 1
        Screen.Notificaciones.route, // 2
        "inventario",                // 3
        Screen.Calendario.route,     // 4
        "controlEmergencia",         // 5
        "perfil"                     // 6
    )

    val mostrarBarrasGlobales = currentRoute != Screen.Login.route &&
            currentRoute != "registrarse" &&
            currentRoute != "restaurar" &&
            currentRoute != "verificar" &&
            currentRoute != "restablecer"

    Scaffold(
        bottomBar = {
            if (mostrarBarrasGlobales) {
                NavigationBar(
                    containerColor = Color(0xFFF1F1F1),
                    tonalElevation = 4.dp
                ) {
                    val navIcons = listOf(
                        R.drawable.ic_inicio,
                        R.drawable.ic_formulario,
                        R.drawable.ic_notificacion,
                        R.drawable.ic_inventario,
                        R.drawable.ic_calendario,
                        R.drawable.ic_emergencia,
                        R.drawable.ic_perfil
                    )

                    navIcons.forEachIndexed { index, iconRes ->
                        val routeTarget = routes.getOrNull(index) ?: Screen.Inicio.route
                        val isSelected = currentRoute == routeTarget

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != routeTarget) {
                                    navController.navigate(routeTarget) {
                                        popUpTo(Screen.Inicio.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Box(modifier = Modifier.wrapContentSize()) {
                                    Image(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = null,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    if (index == 2 && tomasPendientes > 0) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .background(Color.Red, shape = RoundedCornerShape(8.dp))
                                                .align(Alignment.TopEnd),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = tomasPendientes.toString(),
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = if (isSelected) Color(0xFF59CBA2).copy(alpha = 0.3f) else Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            if (mostrarBarrasGlobales) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.logopastillero),
                            contentDescription = null,
                            modifier = Modifier.size(55.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "PILLBOT",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            NavHost(
                navController = navController,
                startDestination = Screen.Login.route,
                modifier = Modifier.weight(1f)
            ) {
                // Login
                composable(route = Screen.Login.route) {
                    Login(
                        onForgotPasswordClick = { navController.navigate("restaurar") },
                        onLoginSuccess = { idObtenido ->
                            navController.navigate(Screen.Inicio.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        onRegisterClick = { navController.navigate("registrarse") }
                    )
                }

                // Registrarse
                composable(route = "registrarse") {
                    Registrarse(
                        onBackToLoginClick = { navController.popBackStack() },
                        onRegisterSuccessClick = { navController.navigate(Screen.Login.route) }
                    )
                }

                // Inicio
                composable(route = Screen.Inicio.route) {
                    Inicio(
                        usuarioId = usuarioIdInicial,
                        nombreUsuario = "Jimena",
                        onIrANotificaciones = { navController.navigate(Screen.Notificaciones.route) },
                        onIrACalendario = { navController.navigate(Screen.Calendario.route) },
                        viewModel = tomaHoyViewModel
                    )
                }

                // Selección de Registro (Cámara / Manual)
                composable(route = "formulario") {
                    FormularioMedicamento(
                        currentScreen = "formulario",
                        totalNoLeidas = tomasPendientes,
                        onNavTabClick = { ruta -> navController.navigate(ruta) },
                        viewModel = medicamentoViewModel
                    )
                }

                // Formulario Manual Detallado
                /*composable(route = "formularioManual") {
                    FormularioManual(
                        currentScreen = "formularioManual",
                        onNavTabClick = { ruta -> navController.navigate(ruta) },
                        viewModel = medicamentoViewModel,
                        recordatorioViewModel = recordatorioViewModel
                    )
                }*/

                // Inventario
                composable(route = "inventario") {
                    // Sincronizamos la lista real del ViewModel en lugar de un listado vacío fijo
                    val listaMedicamentos by medicamentoViewModel.medicamentos

                    LaunchedEffect(usuarioIdInicial) {
                        medicamentoViewModel.cargarMedicamentos(usuarioIdInicial)
                    }

                    InventarioMedicamentosScreen(
                        medicamentos = listaMedicamentos,
                        onMedicamentoClick = { medicamento ->
                            medicamentoViewModel.medicamentoSeleccionado = medicamento
                            navController.navigate("detalleMedicamento")
                        }
                    )
                }

                // Detalle Medicamento
                composable(route = "detalleMedicamento") {
                    val medicamentoSeleccionado = medicamentoViewModel.medicamentoSeleccionado

                    if (medicamentoSeleccionado != null) {
                        DetalleMedicamento(
                            medicamento = medicamentoSeleccionado,
                            onVolver = { navController.popBackStack() },
                            onRecargarClick = { navController.navigate("recargarMedicamento") }
                        )
                    } else {
                        navController.popBackStack()
                    }
                }

                // Calendario
                composable(route = Screen.Calendario.route) {
                    PillBotCalendarScreen(
                        usuarioId = usuarioIdInicial,
                        onVolver = { navController.popBackStack() }
                    )
                }

                // Notificaciones
                composable(route = Screen.Notificaciones.route) {
                    Notificaciones(
                        usuarioId = usuarioIdInicial,
                        onVolver = { navController.popBackStack() },
                        viewModel = tomaHoyViewModel
                    )
                }

                // Emergencias
                composable(route = "controlEmergencia") {
                    ControlEmergencia()
                }

                // Detalle Evento Calendario
                composable(route = "detalleEvento") {
                    PillBotEventDetailScreen(
                        onVolver = { navController.popBackStack() }
                    )
                }

                // Agregar Medicamento Alternativo
                composable(route = "agregarMedicamento") {
                    AgregarMedicamento(
                        onVolver = { navController.popBackStack() }
                    )
                }

                // Recargar stock
                composable(route = "recargarMedicamento") {
                    RecargarMedicamento(
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(route = Screen.Perfil.route) {
                    ConfiguracionPerfil(
                        usuarioId = usuarioIdInicial,
                        onCerrarSesion = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}