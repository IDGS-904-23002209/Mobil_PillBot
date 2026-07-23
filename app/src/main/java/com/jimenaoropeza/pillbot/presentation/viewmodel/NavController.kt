package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.jimenaoropeza.pillbot.pantallas.*
import com.jimenaoropeza.pillbot.presentation.screens.Notificaciones
import com.jimenaoropeza.pillbot.presentation.screens.RegistrarTratamiento
import com.jimenaoropeza.pillbot.viewmodel.MedicamentoViewModel
import com.jimenaoropeza.pillbot.viewmodel.RecordatorioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillBotNavigation(
    navController: NavHostController,
    usuarioIdInicial: Int,
    tomaHoyViewModel: TomaHoyViewModel = viewModel(),
    navegarAInicio: Boolean = false,
    onNavegacionAInicioConsumida: () -> Unit = {}
) {
    // INSTANCIACIÓN DE VIEWMODELS
    val medicamentoViewModel: MedicamentoViewModel = viewModel()
    val recordatorioViewModel: RecordatorioViewModel = viewModel()
    val compartimentoViewModel: CompartimentoViewModel = viewModel()

    // 🟢 CORREGIDO: Ambas variables ahora usan rememberSaveable para asegurar el ID de sesión
    var usuarioId by rememberSaveable { mutableStateOf(usuarioIdInicial) }
    var nombreUsuario by rememberSaveable { mutableStateOf("Usuario") }

    // Monitorea la ruta actual de navegación
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Efecto lanzado globalmente para actualizar el contador de notificaciones pendientes
    LaunchedEffect(usuarioId) {
        if (usuarioId > 0) {
            tomaHoyViewModel.cargarTomasHoy(usuarioId = usuarioId)
        }
    }

    // Navega a Inicio cuando la app se abrió desde una notificación (y hay sesión activa)
    LaunchedEffect(navegarAInicio, usuarioId) {
        if (navegarAInicio && usuarioId > 0) {
            navController.navigate(Screen.Inicio.route) {
                popUpTo(Screen.Inicio.route) { inclusive = false }
                launchSingleTop = true
            }
            onNavegacionAInicioConsumida()
        }
    }

    val tomasHoy by tomaHoyViewModel.tomasHoy
    val tomasPendientes = tomasHoy.count { !it.tomado }

    // Rutas correspondientes a la barra inferior
    val routes = listOf(
        Screen.Inicio.route,
        Screen.Notificaciones.route,
        "historialMedicamento",
        Screen.Calendario.route,
        "controlEmergencia",
        Screen.Perfil.route
    )

    // Determina si se deben mostrar las barras generales
    val mostrarBarrasGlobales =
        currentRoute != Screen.Login.route &&
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
                        R.drawable.ic_notificacion,
                        R.drawable.ic_historialmedicamentos,
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
                                        popUpTo(Screen.Inicio.route) {
                                            saveState = true
                                        }
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

                                    if (index == 1 && tomasPendientes > 0) {
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
                                indicatorColor = if (isSelected) {
                                    Color(0xFF59CBA2).copy(alpha = 0.3f)
                                } else {
                                    Color.Transparent
                                }
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
            // Encabezado superior de PillBot
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
                // LOGIN
                composable(route = Screen.Login.route) {
                    Login(
                        onForgotPasswordClick = { navController.navigate("restaurar") },
                        onLoginSuccess = { idObtenido, nombreObtenido ->
                            usuarioId = idObtenido
                            nombreUsuario = nombreObtenido
                            navController.navigate(Screen.Inicio.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        onRegisterClick = { navController.navigate("registrarse") }
                    )
                }

                // REGISTRO
                composable(route = "registrarse") {
                    Registrarse(
                        onBackToLoginClick = { navController.popBackStack() },
                        onRegisterSuccessClick = { navController.navigate(Screen.Login.route) }
                    )
                }

                // INICIO
                composable(route = Screen.Inicio.route) {
                    Inicio(
                        usuarioId = usuarioId,
                        nombreUsuario = nombreUsuario,
                        onIrANotificaciones = { navController.navigate(Screen.Notificaciones.route) },
                        onIrACalendario = { navController.navigate(Screen.Calendario.route) },
                        onIrATratamiento = { navController.navigate(Screen.Tratamiento.route) },
                        onIrAProgramacion = { navController.navigate(Screen.ProgramacionTratamiento.route) },
                        viewModel = tomaHoyViewModel
                    )
                }

                // HISTORIAL DE MEDICAMENTOS
                composable(route = "historialMedicamento") {
                    val listaMedicamentos by medicamentoViewModel.medicamentos

                    // 🛡️ CORREGIDO: Lanzar solo si el ID de usuario es válido
                    LaunchedEffect(usuarioId) {
                        if (usuarioId > 0) {
                            medicamentoViewModel.cargarMedicamentos(usuarioId)
                        }
                    }

                    InventarioMedicamentosScreen(
                        medicamentos = listaMedicamentos,
                        onMedicamentoClick = { medicamento ->
                            medicamentoViewModel.medicamentoSeleccionado = medicamento
                            navController.navigate("detalleMedicamento")
                        },
                        onVerCompartimentos = { navController.navigate("compartimentos") }
                    )
                }

                // PANTALLA DE COMPARTIMENTOS
                // PANTALLA DE COMPARTIMENTOS
                composable(route = "compartimentos") {
                    val listaCompartimentos by compartimentoViewModel.compartimentos
                    val estaCargando by compartimentoViewModel.cargando

                    LaunchedEffect(usuarioId) {
                        if (usuarioId > 0) {
                            compartimentoViewModel.cargarCompartimentosUsuario(idUsuario = usuarioId)
                        }
                    }

                    CompartimentosScreen(
                        compartimentos = listaCompartimentos,
                        cargando = estaCargando,
                        onVolver = { navController.popBackStack() },
                        onGuardarCantidad = { idCompartimento, nuevaCantidad, onResult ->
                            compartimentoViewModel.actualizarCantidadActual(idCompartimento, nuevaCantidad, onResult)
                        }
                    )
                }

                // DETALLE DE MEDICAMENTO
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

                // CALENDARIO
                composable(route = Screen.Calendario.route) {
                    PillBotCalendarScreen(
                        usuarioId = usuarioId,
                        onVolver = { navController.popBackStack() }
                    )
                }

                // NOTIFICACIONES
                composable(route = Screen.Notificaciones.route) {
                    Notificaciones(
                        usuarioId = usuarioId,
                        onVolver = { navController.popBackStack() },
                        viewModel = tomaHoyViewModel
                    )
                }

                // CONTROL DE EMERGENCIA
                composable(route = "controlEmergencia") {
                    ControlEmergencia()
                }

                // DETALLE DEL EVENTO
                composable(route = "detalleEvento") {
                    PillBotEventDetailScreen(
                        onVolver = { navController.popBackStack() }
                    )
                }

                // RECARGAR MEDICAMENTO
                composable(route = "recargarMedicamento") {
                    val medicamentoSeleccionado = medicamentoViewModel.medicamentoSeleccionado
                    if (medicamentoSeleccionado != null) {
                        RecargarMedicamento(
                            medicamento = medicamentoSeleccionado,
                            onVolver = { navController.popBackStack() }
                        )
                    } else {
                        navController.popBackStack()
                    }
                }

                // PROGRAMACIÓN DE TRATAMIENTO
                composable(route = Screen.ProgramacionTratamiento.route) {
                    RegistrarTratamiento(
                        usuarioId = usuarioId,
                        onVolver = { navController.popBackStack() },
                        onGuardadoExitoso = { navController.popBackStack() }
                    )
                }

                // PERFIL
                composable(route = Screen.Perfil.route) {
                    ConfiguracionPerfil(
                        usuarioId = usuarioId,
                        onCerrarSesion = {
                            // Al cerrar sesión limpiamos los estados explícitamente
                            usuarioId = -1
                            nombreUsuario = "Usuario"
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                composable(route = "restaurar") {}
                composable(route = "verificar") {}
                composable(route = "restablecer") {}
            }
        }
    }
}