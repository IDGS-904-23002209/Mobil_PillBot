package com.jimenaoropeza.pillbot.modelo

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: UsuarioLogin?,
    val error: String?
)