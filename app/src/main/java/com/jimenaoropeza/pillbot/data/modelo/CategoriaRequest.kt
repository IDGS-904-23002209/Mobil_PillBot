package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class Categoria(

    @SerializedName("idCategoria")
    val idCategoria: Int,

    @SerializedName("nombreCategoria")
    val nombreCategoria: String,

    @SerializedName("descripcion")
    val descripcion: String?
)