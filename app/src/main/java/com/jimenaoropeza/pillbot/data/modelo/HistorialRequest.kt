package com.jimenaoropeza.pillbot.data.modelo

data class HistorialRequest(
    val IdToma: Int,
    val FechaReal: String,       // Enviará la fecha en formato ISO completo
    val HoraReal: String,        // Enviará la hora en formato ISO completo
    val Estatus: String,
    val PesoDetectado: Double = 0.0,
    val ConfirmadaPorIr: Boolean = false
)