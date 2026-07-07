package com.jimenaoropeza.pillbot.data.repository

import com.jimenaoropeza.pillbot.data.modelo.CompartimentoRequest
import com.jimenaoropeza.pillbot.network.RetrofitInstance

class CompartimentoRepository {

    private val api = RetrofitInstance.api

    suspend fun obtenerCompartimentos(): List<CompartimentoRequest> {
        return api.obtenerCompartimentos()
    }
}