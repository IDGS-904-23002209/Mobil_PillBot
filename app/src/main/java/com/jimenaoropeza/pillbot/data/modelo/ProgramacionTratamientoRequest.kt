package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class ProgramacionTratamientoRequest(
    @SerializedName("idProgramacion") val idProgramacion: Int? = null,
    @SerializedName("idDetalleReceta") val idDetalleReceta: Int?,
    @SerializedName("idCompartimento") val idCompartimento: Int?,
    @SerializedName("horaInicio") val horaInicio: String? = null, // Formato "HH:mm:ss"
    @SerializedName("pesoEsperadoGramos") val pesoEsperadoGramos: Double? = null, // <-- default null
    @SerializedName("diaInicio") val diaInicio: String? = null,   // Formato ISO "yyyy-MM-dd"
    @SerializedName("diaFin") val diaFin: String? = null,         // Formato ISO "yyyy-MM-dd"
    @SerializedName("activo") val activo: Boolean? = true
)