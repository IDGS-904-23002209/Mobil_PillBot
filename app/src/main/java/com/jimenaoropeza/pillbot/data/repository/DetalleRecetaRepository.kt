package com.jimenaoropeza.pillbot.data.repository

import com.jimenaoropeza.pillbot.data.modelo.DetalleRecetaRequest
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import retrofit2.Response

class DetalleRecetaRepository {

    private val api = RetrofitInstance.api

    suspend fun consultarDetallesPorReceta(idReceta: Int): Response<List<DetalleRecetaRequest>> {
        return api.consultarDetallesPorReceta(idReceta)
    }
}