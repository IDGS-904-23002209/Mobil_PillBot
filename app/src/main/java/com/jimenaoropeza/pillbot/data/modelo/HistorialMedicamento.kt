package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class HistorialMedicamento(
    @SerializedName("idReceta")
    val idReceta: Int,

    @SerializedName("idMedicamento")
    val idMedicamento: Int,

    @SerializedName("nombreComercial")
    val nombreMedicamento: String,

    @SerializedName("principioActivo")
    val principioActivo: String,

    @SerializedName("padecimiento")
    val padecimiento: String,

    @SerializedName("fechaInicio")
    val fechaInicio: String
)