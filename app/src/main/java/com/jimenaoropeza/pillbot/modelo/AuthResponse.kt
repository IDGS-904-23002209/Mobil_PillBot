package com.jimenaoropeza.pillbot.modelo

data class AuthResponse(
    val token: String?,
    val idUsuario: Int,
    val correo: String,
    val nombre: String,
    val idRol: Int
)