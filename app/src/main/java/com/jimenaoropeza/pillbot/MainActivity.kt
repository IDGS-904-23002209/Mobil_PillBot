package com.jimenaoropeza.pillbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.jimenaoropeza.pillbot.pantallas.Login
import com.jimenaoropeza.pillbot.pantallas.ForgotPassword
import com.jimenaoropeza.pillbot.pantallas.Inicio
import com.jimenaoropeza.pillbot.pantallas.Notificaciones
import com.jimenaoropeza.pillbot.pantallas.Registrarse
import com.jimenaoropeza.pillbot.pantallas.ResetPassword
import com.jimenaoropeza.pillbot.pantallas.VerificacionCode
import com.jimenaoropeza.pillbot.pantallas.ControlEmergencia
import com.jimenaoropeza.pillbot.pantallas.InventarioMedicamentos
import com.jimenaoropeza.pillbot.pantallas.DetalleMedicamento
import com.jimenaoropeza.pillbot.pantallas.RecargarMedicamento
import com.jimenaoropeza.pillbot.pantallas.AgregarMedicamento
import com.jimenaoropeza.pillbot.pantallas.ConfiguracionPerfil
import com.jimenaoropeza.pillbot.pantallas.FormularioManual
import com.jimenaoropeza.pillbot.pantallas.FormularioMedicamento
import com.jimenaoropeza.pillbot.pantallas.PillBotCalendarScreen
import com.jimenaoropeza.pillbot.pantallas.PillBotEventDetailScreen
import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.viewmodel.MedicamentoViewModel
import com.jimenaoropeza.pillbot.viewmodel.RecordatorioViewModel
import com.jimenaoropeza.pillbot.viewmodel.AuthViewModel

data class NotificacionItem(
    val id: Int,
    val imageRes: Int,
    val title: String,
    val desc: String,
    val time: String,
    var esLeida: Boolean,
    val rutaDestino: String? = null
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Puedes cambiarlo a "login" para producción o dejarlo en "inicio" para tus pruebas directas
            var currentScreen by remember { mutableStateOf("inicio") }

            val listaNotificaciones = remember {
                mutableStateListOf(
                    NotificacionItem(1, R.drawable.ic_pildora, "¡Hora de tomar tu medicamento!", "Recuerda tomar tu pastilla de las 10:00 AM (Aspirina)", "Hace 9 min.", false, "calendario"),
                    NotificacionItem(2, R.drawable.iconowarning, "Nivel bajo de pastillas:\nCompartimiento 3", "Recarga pronto para evitar interrupciones.", "Hace 25 min.", false, "inventario"),
                    NotificacionItem(3, R.drawable.iconopalomita, "El dispensador está listo", "Ciclo de autolimpieza completado con éxito", "Hace 1 hora", true, null),
                    NotificacionItem(4, R.drawable.botepass, "Confirmación de recarga recibida.", "Tu recarga de Atorvastatina ha sido procesada.", "Ayer, 3:30 PM", true, null),
                    NotificacionItem(5, R.drawable.tabla, "Tu historial de medicación de ayer.", "El resumen diario está disponible para su revisión", "Ayer, 11:59 PM", false, "inicio")
                )
            }

            val totalNoLeidas = listaNotificaciones.count { !it.esLeida }
            val authViewModel: AuthViewModel = viewModel()
            val medicamentoViewModel: MedicamentoViewModel = viewModel()
            val recordatorioViewModel: RecordatorioViewModel = viewModel()

            var medicamentoSeleccionado by remember {
                mutableStateOf<Medicamento?>(null)
            }

            when (currentScreen) {
                "login" -> {
                    Login(
                        viewModel = authViewModel,
                        onForgotPasswordClick = { currentScreen = "restaurar" },
                        onLoginSuccessClick = { _ ->
                            currentScreen = "inicio"
                        },
                        onRegisterClick = { currentScreen = "registrarse" }
                    )
                }
                "registrarse" -> {
                    Registrarse(
                        viewModel = authViewModel,
                        onBackToLoginClick = { currentScreen = "login" },
                        onRegisterSuccessClick = { currentScreen = "login" }
                    )
                }
                "inicio" -> {
                    // Si la app inicia directo aquí o el backend tarda, se inyectan tus credenciales por defecto para que funcione
                    val nombreUsuario = if (authViewModel.usuarioNombre == "Usuario") "Jimena" else authViewModel.usuarioNombre
                    val idUsuario = if (authViewModel.usuarioId == 0) 5 else authViewModel.usuarioId

                    Inicio(
                        currentScreen = currentScreen,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { route -> currentScreen = route },
                        usuarioId = idUsuario,
                        nombreUsuario = nombreUsuario
                    )
                }
                "restaurar" -> {
                    ForgotPassword(
                        onBackToLogin = { currentScreen = "login" },
                        onCodeSent = { currentScreen = "verificar" }
                    )
                }
                "verificar" -> {
                    VerificacionCode(
                        onVerifyCodeClick = { currentScreen = "restablecer" }
                    )
                }
                "restablecer" -> {
                    ResetPassword(
                        onSaveAndAccessClick = { currentScreen = "login" }
                    )
                }
                "notificaciones" -> {
                    Notificaciones(
                        currentScreen = currentScreen,
                        listaNotificaciones = listaNotificaciones,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { destination -> currentScreen = destination }
                    )
                }
                "controlEmergencia" -> {
                    ControlEmergencia(
                        currentScreen = currentScreen,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { destination -> currentScreen = destination }
                    )
                }
                "formulario" -> {
                    FormularioMedicamento(
                        currentScreen = currentScreen,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { destination -> currentScreen = destination },
                        viewModel = medicamentoViewModel
                    )
                }
                "formularioManual" -> {
                    FormularioManual(
                        currentScreen = currentScreen,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { destination -> currentScreen = destination },
                        viewModel = medicamentoViewModel,
                        recordatorioViewModel = recordatorioViewModel
                    )
                }
                "perfil" -> {
                    ConfiguracionPerfil(
                        currentScreen = currentScreen,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { destination -> currentScreen = destination },
                        onCerrarSesion = { currentScreen = "login" }
                    )
                }
                "inventario" -> {
                    InventarioMedicamentos(
                        currentScreen = currentScreen,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { destination -> currentScreen = destination },
                        onMedicamentoClick = { medicamento ->
                            medicamentoSeleccionado = medicamento
                            currentScreen = "detalleMedicamento"
                        },
                        onAgregarMedicamentoClick = {
                            currentScreen = "formulario"
                        },
                        viewModel = medicamentoViewModel
                    )
                }
                "detalleMedicamento" -> {
                    medicamentoSeleccionado?.let { medicamento ->
                        DetalleMedicamento(
                            currentScreen = currentScreen,
                            totalNoLeidas = totalNoLeidas,
                            onNavTabClick = { destination -> currentScreen = destination },
                            onRecargarClick = { currentScreen = "recargarMedicamento" },
                            medicamento = medicamento
                        )
                    }
                }
                "recargarMedicamento" -> {
                    RecargarMedicamento(
                        currentScreen = currentScreen,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { destination -> currentScreen = destination }
                    )
                }
                "agregarMedicamento" -> {
                    AgregarMedicamento(
                        currentScreen = currentScreen,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { destination -> currentScreen = destination }
                    )
                }
                "calendario" -> {
                    PillBotCalendarScreen(
                        currentScreen = currentScreen,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { destination -> currentScreen = destination },
                        onDiaClick = { currentScreen = "detalleEvento" }
                    )
                }
                "detalleEvento" -> {
                    PillBotEventDetailScreen(
                        currentScreen = currentScreen,
                        totalNoLeidas = totalNoLeidas,
                        onNavTabClick = { destination -> currentScreen = destination }
                    )
                }
            }
        }
    }
}