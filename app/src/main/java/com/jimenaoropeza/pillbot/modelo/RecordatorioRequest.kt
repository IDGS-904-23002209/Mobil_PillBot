package com.jimenaoropeza.pillbot.modelo

data class RecordatorioRequest(
    val id_usuario: Int,
    val id_medicamento: Int,
    val hora_toma: String,
    val frecuencia_horas: Int,
    val dosis: String,
    val dias_tratamiento: Int
)