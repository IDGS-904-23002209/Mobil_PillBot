package com.jimenaoropeza.pillbot.data.modelo

import com.google.gson.annotations.SerializedName

data class ActualizarClienteRequest(
    @SerializedName("idCliente") val idCliente: Int,
    @SerializedName("usuarioId") val usuarioId: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidoPaterno") val apellidoPaterno: String,
    @SerializedName("apellidoMaterno") val apellidoMaterno: String? = null,
    @SerializedName("correo") val correo: String,
    @SerializedName("contrasena") val contrasena: String? = null,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("fechaNacimiento") val fechaNacimiento: String? = null,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("tipoSangre") val tipoSangre: String? = null,
    @SerializedName("alergias") val alergias: String? = null,
    @SerializedName("contactoEmergencia") val contactoEmergencia: String? = null,
    @SerializedName("telefonoEmergencia") val telefonoEmergencia: String? = null
)