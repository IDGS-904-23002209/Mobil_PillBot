package com.jimenaoropeza.pillbot.modelo

data class Medicamento(
    val id_medicamento: Int,
    val nombre_medicamento: String,
    val gramaje_medicamento: String,
    val inventario_actual: Int,
    val descripcion: String
)