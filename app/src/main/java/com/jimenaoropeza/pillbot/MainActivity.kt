package com.jimenaoropeza.pillbot

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.jimenaoropeza.pillbot.presentation.components.NotificationHelper
import com.jimenaoropeza.pillbot.presentation.viewmodel.PillBotNavigation

class MainActivity : ComponentActivity() {

    // Launcher para pedir permiso de notificaciones en Android 13+
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            // El usuario rechazó las notificaciones, manejar de ser necesario
        }
    }

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

        setContent {
            // Inicializamos el NavController oficial de Jetpack Compose
            val navController = rememberNavController()

            // Centralización dinámica del ID del usuario actual de la sesión
            // Le pasamos por defecto el ID 5 como lo tenías configurado
            val usuarioIdDinamico = 3

            // Ejecutamos tu enrutador limpio, el cual se encargará de gestionar
            // las pantallas (Login, Inicio, Calendario, Notificaciones, etc.)
            PillBotNavigation(
                navController = navController,
                usuarioIdInicial = usuarioIdDinamico
            )
        }
    }
}