package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("contrasena") val contrasena: String,
    @SerializedName("dispositivo") val dispositivo: String,
    @SerializedName("ipOrigen") val ipOrigen: String,
    @SerializedName("detallesNavegador") val detallesNavegador: String
)