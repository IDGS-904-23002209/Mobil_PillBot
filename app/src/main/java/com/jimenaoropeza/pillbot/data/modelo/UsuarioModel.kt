package com.jimenaoropeza.pillbot.data.modelo

import com.google.gson.annotations.SerializedName

data class UsuarioResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: UsuarioDto?
)

data class UsuarioDto(
    @SerializedName("idUsuario") val idUsuario: Int,
    @SerializedName("idPersona") val idPersona: Int,
    @SerializedName("idRol") val idRol: Int,
    @SerializedName("persona") val persona: PersonaDto?,
    @SerializedName("rol") val rol: RolDto?
)

data class PersonaDto(
    @SerializedName("idPersona") val idPersona: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidoPaterno") val apellidoPaterno: String,
    @SerializedName("apellidoMaterno") val apellidoMaterno: String?,
    @SerializedName("fechaNacimiento") val fechaNacimiento: String?,
    @SerializedName("telefono") val telefono: String?,
    @SerializedName("correo") val correo: String,
    @SerializedName("direccion") val direccion: String?
)

data class RolDto(
    @SerializedName("idRol") val idRol: Int,
    @SerializedName("nombreRol") val nombreRol: String,
    @SerializedName("descripcion") val descripcion: String?
)
