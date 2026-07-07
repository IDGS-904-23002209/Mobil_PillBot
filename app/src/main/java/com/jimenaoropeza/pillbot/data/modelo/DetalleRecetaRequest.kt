package com.jimenaoropeza.pillbot.data.modelo


import com.google.gson.annotations.SerializedName

data class DetalleRecetaRequest(
    @SerializedName("idDetalleReceta") val idDetalleReceta: Int?,
    @SerializedName("idReceta") val idReceta: Int?,
    @SerializedName("idMedicamento") val idMedicamento: Int?,
    @SerializedName("dosis") val dosis: String?,
    @SerializedName("indicaciones") val indicaciones: String?,
    @SerializedName("frecuenciaHoras") val frecuenciaHoras: Int?,
    @SerializedName("duracionDias") val duracionDias: Int?
)