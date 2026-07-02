package com.jimenaoropeza.pillbot.modelo

data class LoginRequest(
    val correo: String,
    val contrasena: String,
    val dispositivo: String,
    val ipOrigen: String,
    val detallesNavegador: String
)