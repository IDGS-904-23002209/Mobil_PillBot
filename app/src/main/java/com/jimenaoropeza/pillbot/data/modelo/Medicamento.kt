package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class Medicamento(
    @SerializedName("idTratamiento")
    val idTratamiento: Int,

    @SerializedName("idMedicamento")
    val idMedicamento: Int,

    @SerializedName("nombreComercial")
    val nombreMedicamento: String, // Cambiado para que sea descriptivo y use camelCase estándar

    @SerializedName("principioActivo")
    val principioActivo: String, // ¡Corregido! Ya no se confunde con el gramaje

    @SerializedName("cantidadActualPastillas")
    val inventarioActual: Int,

    @SerializedName("numeroCompartimento")
    val numeroCompartimento: Int, // Agregado porque es vital para saber en qué cajón de PillBot está

    @SerializedName("estadoCompartimento")
    val estadoCompartimento: String // Refleja fielmente si está "Ocupado" o "Vacío"
)