package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class Medicamento(
    @SerializedName("idMedicamento")
    val idMedicamento: Int,

    @SerializedName("nombreComercial")
    val nombre_medicamento: String,

    @SerializedName("principioActivo")
    val gramaje_medicamento: String,

    @SerializedName("cantidadActualPastillas")
    val inventario_actual: Int,

    @SerializedName("estadoCompartimento")
    val descripcion: String
)