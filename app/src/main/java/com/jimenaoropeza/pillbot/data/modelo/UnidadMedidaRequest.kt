package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class UnidadMedida(

    @SerializedName("idUnidadMedida")
    val idUnidadMedida: Int,

    @SerializedName("nombreUnidad")
    val nombreUnidad: String,

    @SerializedName("abreviatura")
    val abreviatura: String?,

    @SerializedName("descripcion")
    val descripcion: String?
)