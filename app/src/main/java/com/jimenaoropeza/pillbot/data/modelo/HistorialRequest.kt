package com.jimenaoropeza.pillbot.modelo

data class HistorialRequest(
    val id_recordatorio: Int,
    val fecha_registro: String,
    val hora_registro: String,
    val estatus: String
)