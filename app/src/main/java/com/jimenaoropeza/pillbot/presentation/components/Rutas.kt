package com.jimenaoropeza.pillbot.presentation.components

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Registrarse : Screen("registrarse")
    object Inicio : Screen("inicio")
    object Calendario : Screen("calendario")
    object Notificaciones : Screen("notificaciones")
    object Perfil : Screen("perfil")
}