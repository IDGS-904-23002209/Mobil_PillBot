package com.jimenaoropeza.pillbot.repository

import com.jimenaoropeza.pillbot.data.modelo.HistorialRequest
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.network.ApiService
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TomaHoyRepository {
    private val apiService: ApiService = RetrofitInstance.api

    suspend fun obtenerTomasHoy(usuarioId: Int): List<TomaHoy> {
        return try {
            apiService.obtenerTomasHoy(usuarioId)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun marcarMedicamentoTomado(idToma: Int): Boolean {
        return try {
            val ahora = LocalDateTime.now()

            // Formateamos la fecha a ISO8601 básica para el backend: ej. "2026-07-14T10:30:00Z"
            val fechaFormateada = ahora.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z"

            // Formateamos la hora en un esquema estricto de TimeSpan (HH:mm:ss): ej. "10:30:00"
            val horaFormateada = ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

            // CORREGIDO: Usamos las minúsculas exactas del modelo HistorialRequest
            val historialRequest = HistorialRequest(
                idToma = idToma,
                fechaReal = fechaFormateada,
                horaReal = horaFormateada,
                estatus = "Tomado",
                pesoDetectado = 0.0,
                confirmadaPorIr = false
            )

            // Consumimos el endpoint corregido de tu ApiService
            val respuesta = apiService.registrarToma(historialRequest)
            respuesta.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}