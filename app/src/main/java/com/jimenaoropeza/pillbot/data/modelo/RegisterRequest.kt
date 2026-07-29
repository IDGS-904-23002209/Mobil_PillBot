package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellidoPaterno")
    val apellidoPaterno: String,

    @SerializedName("apellidoMaterno")
    val apellidoMaterno: String,

    @SerializedName("correo")
    val correo: String,

    @SerializedName("contrasena")
    val contrasena: String,

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("idRol")
    val idRol: Int
)