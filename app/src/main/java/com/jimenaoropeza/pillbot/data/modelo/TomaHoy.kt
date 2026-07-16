package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class TomaHoy(
    @SerializedName("idToma")
    val id_toma: Int,

    @SerializedName("horaToma")
    val hora_toma: String?,

    @SerializedName("nombreMedicamento")
    val nombre_medicamento: String?,

    @SerializedName("dosis")
    val dosis: String?,

    @SerializedName("tomado")
    val tomado: Boolean
)