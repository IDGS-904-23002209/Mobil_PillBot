package com.jimenaoropeza.pillbot

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.jimenaoropeza.pillbot.presentation.components.NotificationHelper
import com.jimenaoropeza.pillbot.presentation.viewmodel.PillBotNavigation
import androidx.activity.result.contract.ActivityResultContracts
import com.jimenaoropeza.pillbot.notificaciones.EXTRA_ABRIR_INICIO


class MainActivity : ComponentActivity() {

    // Launcher para solicitar el permiso de notificaciones (Android 13+)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* concedido o no */ }

    // Estado que indica si debemos navegar a Inicio (por ejemplo, al tocar una notificación)
    private val abrirInicioState = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Crear canal de notificaciones corporativo
        NotificationHelper.crearCanalNotificaciones(this)

        // 2. Solicitar permisos si es Android 13 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Revisamos si la Activity se abrió desde una notificación
        abrirInicioState.value = intent?.getBooleanExtra(EXTRA_ABRIR_INICIO, false) == true

        setContent {
            val navController = rememberNavController()

            var usuarioIdDinamico by rememberSaveable { mutableStateOf(-1) }

            val abrirInicio by abrirInicioState

            PillBotNavigation(
                navController = navController,
                usuarioIdInicial = usuarioIdDinamico,
                navegarAInicio = abrirInicio,
                onNavegacionAInicioConsumida = { abrirInicioState.value = false }
            )
        }
    }

    // Si la Activity ya estaba abierta (singleTop/singleTask), este método recibe el nuevo intent
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent.getBooleanExtra(EXTRA_ABRIR_INICIO, false)) {
            abrirInicioState.value = true
        }
    }
}