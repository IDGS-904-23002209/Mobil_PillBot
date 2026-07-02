package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class TipoPresentacion(

    @SerializedName("idPresentacion")
    val idPresentacion: Int,

    @SerializedName("nombrePresentacion")
    val nombrePresentacion: String,

    @SerializedName("descripcion")
    val descripcion: String?
)