package com.jimenaoropeza.pillbot.repository

import com.jimenaoropeza.pillbot.modelo.HistorialRequest
import com.jimenaoropeza.pillbot.network.RetrofitInstance

class HistorialRepository {

    private val api = RetrofitInstance.api

    suspend fun registrarToma(
        historial: HistorialRequest
    ): HistorialRequest {
        return api.registrarToma(historial)
    }
}