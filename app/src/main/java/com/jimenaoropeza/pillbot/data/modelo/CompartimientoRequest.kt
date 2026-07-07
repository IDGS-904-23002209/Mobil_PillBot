package com.jimenaoropeza.pillbot.data.modelo



import com.google.gson.annotations.SerializedName

data class CompartimentoRequest(
    @SerializedName("idCompartimento") val idCompartimento: Int,
    @SerializedName("idDispositivoPill") val idDispositivoPill: Int,
    @SerializedName("numeroCompartimento") val numeroCompartimento: Int,
    @SerializedName("capacidadMaximaPastillas") val capacidadMaximaPastillas: Int,
    @SerializedName("cantidadActualPastillas") val cantidadActualPastillas: Int,
    @SerializedName("estado") val estado: String?
)