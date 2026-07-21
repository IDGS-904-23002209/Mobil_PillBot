package com.jimenaoropeza.pillbot.data.repository

import com.jimenaoropeza.pillbot.data.modelo.ActualizarCantidadRequest
import com.jimenaoropeza.pillbot.data.modelo.CompartimentoRequest
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import retrofit2.Response

class CompartimentoRepository {

    private val api = RetrofitInstance.api

    suspend fun obtenerCompartimentos(): List<CompartimentoRequest> {
        return api.obtenerCompartimentos()
    }
    suspend fun obtenerCompartimentosUsuario(
        idUsuario: Int
    ): List<CompartimentoRequest> {
        return api.obtenerCompartimentosUsuario(idUsuario)
    }

    suspend fun actualizarCantidadActual(
        idCompartimento: Int,
        cantidad: Int
    ): Response<Unit> {
        return api.actualizarCantidadCompartimento(
            idCompartimento,
            ActualizarCantidadRequest(cantidad)
        )
    }
}