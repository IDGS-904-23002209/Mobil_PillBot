package com.jimenaoropeza.pillbot.modelo

data class RegisterRequest(
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val correo: String,
    val contrasena: String,
    val telefono: String,
    val idRol: Int
)