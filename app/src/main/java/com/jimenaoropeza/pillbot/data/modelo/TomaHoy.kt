package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class TomaHoy(
    @SerializedName("idToma")
    val id_toma: Int?, // Cambiado a Int? porque la API lo manda null inicialmente

    @SerializedName("idProgramacion")
    val idProgramacion: Int, // Agregado porque lo necesitaremos para registrar la toma

    @SerializedName("horaToma")
    val fecha_programada: String?, // Mapeado directamente a tu propiedad de la UI

    @SerializedName("nombreMedicamento")
    val nombre_medicamento: String?,

    @SerializedName("dosis")
    val dosis: String?,

    @SerializedName("tomado")
    val estado: Boolean?, // Cambiado a Boolean porque la API manda true/false directamente

    @SerializedName("numeroCompartimento")
    val numeroCompartimento: Int?
) {
    // Retorna si ya fue tomado basándose en el booleano real de tu API
    val tomado: Boolean
        get() = estado == true

    // Como tu API ya te manda la hora limpia ("20:15:54"), solo tomamos los primeros 5 caracteres
    val hora_toma: String
        get() {
            if (fecha_programada.isNullOrBlank()) return "--:--"
            return try {
                if (fecha_programada.contains(":")) {
                    fecha_programada.substring(0, 5) // Corta "20:15:54" y lo deja en "20:15"
                } else {
                    fecha_programada
                }
            } catch (e: Exception) {
                "--:--"
            }
        }
}