package com.jimenaoropeza.pillbot.repository

import android.util.Log
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
            val resultado = apiService.obtenerTomasHoy(usuarioId)

            // Log de control para verificar la conexión directa a producción
            Log.d("PILLBOT_API", "Petición realizada para el usuarioId: $usuarioId")
            Log.d(
                "PILLBOT_API",
                "Número de registros reales devueltos por la nube: ${resultado.size}"
            )

            resultado
        } catch (e: Exception) {
            Log.e("PILLBOT_API", "Error crítico en la llamada de red: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun marcarMedicamentoTomado(
        idToma: Int,
        idProgramacion: Int,
        horaToma: String
    ): Boolean {
        return try {
            val historialRequest = HistorialRequest(
                idToma = idToma,
                idProgramacion = idProgramacion,
                horaToma = horaToma,
                estatus = "Tomado",
                pesoDetectado = 0.0,
                confirmadaPorIr = false
            )

            val respuesta = apiService.registrarToma(historialRequest)
            respuesta.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}