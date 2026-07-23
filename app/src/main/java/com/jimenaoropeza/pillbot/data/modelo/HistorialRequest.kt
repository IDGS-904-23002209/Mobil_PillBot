package com.jimenaoropeza.pillbot.data.modelo

import com.google.gson.annotations.SerializedName


data class HistorialRequest(
    @SerializedName("idToma")
    val idToma: Int,

    // Usado por el flujo de Inicio.kt (coincide con el modelo C# Historial)
    @SerializedName("idProgramacion")
    val idProgramacion: Int? = null,

    @SerializedName("horaToma")
    val horaToma: String? = null,

    // Se mantienen porque Calendario.kt (ListView) los sigue usando
    @SerializedName("fechaReal")
    val fechaReal: String? = null,

    @SerializedName("horaReal")
    val horaReal: String? = null,

    @SerializedName("estatus")
    val estatus: String,

    @SerializedName("pesoDetectado")
    val pesoDetectado: Double = 0.0,

    @SerializedName("confirmadaPorIr")
    val confirmadaPorIr: Boolean = false,

    @SerializedName("idDetalleTratamiento")
    val idDetalleTratamiento: Int? = null
)