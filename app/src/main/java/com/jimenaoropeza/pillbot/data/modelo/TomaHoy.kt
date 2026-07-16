package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class TomaHoy(
    @SerializedName("id_toma")
    val id_toma: Int,

    // Se mapea al campo de fecha/hora programada de tu Set 2
    @SerializedName("fecha_programada")
    val fecha_programada: String?,

    @SerializedName("nombre_medicamento")
    val nombre_medicamento: String?,


    @SerializedName("dosis")
    val dosis: String?,

    @SerializedName("estado")
    val estado: String?
) {
    // Propiedad calculada en Kotlin para saber si ya se tomó sin alterar el tipo de dato que viene de SQL
    val tomado: Boolean
        get() = estado?.equals("Tomado", ignoreCase = true) == true

    // Formateador rápido para mostrar solo la hora en la interfaz
    val hora_toma: String
        get() {
            if (fecha_programada == null) return "--:--"
            return try {
                // Si viene como "7/14/2026 12:00:00 AM", extraemos la hora
                if (fecha_programada.contains(" ")) {
                    fecha_programada.substringAfter(" ")
                } else {
                    fecha_programada
                }
            } catch (e: Exception) {
                "--:--"
            }
        }
}