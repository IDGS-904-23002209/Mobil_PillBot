package com.jimenaoropeza.pillbot.modelo

data class UsuarioLogin(
    val idUsuario: Int,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val correo: String,
    val telefono: String,
    val nombreRol: String,
    val idSesion: Int,
    val tokenSesion: String,
    val mensaje: String
)