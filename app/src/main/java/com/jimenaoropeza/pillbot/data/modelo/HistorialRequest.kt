package com.jimenaoropeza.pillbot.data.modelo

import com.google.gson.annotations.SerializedName

data class HistorialRequest(
    @SerializedName("id_toma")
    val idToma: Int,

    @SerializedName("fecha_real")
    val fechaReal: String,       // Formato ISO string (ej. "2026-07-16T12:00:00")

    @SerializedName("hora_real")
    val horaReal: String,

    @SerializedName("estado") // o "estatus" dependiendo de tu tabla Historial (en la captura SQL era 'estado')
    val estatus: String,

    @SerializedName("peso_detectado")
    val pesoDetectado: Double = 0.0,

    @SerializedName("confirmada_por_ir")
    val confirmadaPorIr: Boolean = false,

    @SerializedName("id_detalle_tratamientoo") // Añadir el ID relacional si el backend lo requiere obligatoriamente
    val idDetalleTratamiento: Int? = null
)