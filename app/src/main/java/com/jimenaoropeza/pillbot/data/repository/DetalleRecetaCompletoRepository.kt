package com.jimenaoropeza.pillbot.data.repository

import com.jimenaoropeza.pillbot.data.modelo.DetalleRecetaCompletoRequest
import com.jimenaoropeza.pillbot.network.RetrofitInstance

class DetalleRecetaCompletoRepository {

    private val api = RetrofitInstance.api

    suspend fun obtenerDetallesCompletos(
        soloDisponibles: Boolean = true,
        idUsuario: Int
    ): List<DetalleRecetaCompletoRequest> {
        return api.obtenerDetallesRecetaCompletos(soloDisponibles, idUsuario)
    }
}