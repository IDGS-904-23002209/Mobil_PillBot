package com.jimenaoropeza.pillbot.repository

import com.jimenaoropeza.pillbot.data.modelo.HistorialRequest
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import retrofit2.Response

class HistorialRepository {

    private val api = RetrofitInstance.api

    // Cambiamos el tipo de retorno aquí a Response<Unit> para que calce perfecto con Retrofit
    suspend fun registrarToma(
        historial: HistorialRequest
    ): Response<Unit> {
        return api.registrarToma(historial)
    }
}