package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellidoPaterno")
    val apellidoPaterno: String,

    @SerializedName("apellidoMaterno")
    val apellidoMaterno: String? = null,

    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String, // Formato "YYYY-MM-DD" o ISO

    @SerializedName("correo")
    val correo: String,

    @SerializedName("contrasena")
    val contrasena: String,

    @SerializedName("telefono")
    val telefono: String? = null,

    @SerializedName("direccion")
    val direccion: String,

    @SerializedName("idRol")
    val idRol: Int = 3,

    // Registro de cliente
    @SerializedName("tipoSangre")
    val tipoSangre: String? = null,

    @SerializedName("alergias")
    val alergias: String? = null,

    @SerializedName("contactoEmergencia")
    val contactoEmergencia: String? = null,

    @SerializedName("telefonoEmergencia")
    val telefonoEmergencia: String? = null
)