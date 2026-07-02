package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class TomaHoy(
    @SerializedName("idToma")
    val id_toma: Int,

    @SerializedName("horaToma")
    val hora_toma: String?,

    @SerializedName("nombreMedicamento")
    val nombre_medicamento: String?,

    // Aquí mapeamos "cantidadDosis" de tu API a tu variable "dosis"
    @SerializedName("cantidadDosis")
    val dosis: String?,

    @SerializedName("tomado")
    val tomado: Boolean
)