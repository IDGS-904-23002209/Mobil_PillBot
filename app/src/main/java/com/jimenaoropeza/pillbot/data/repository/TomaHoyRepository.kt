package com.jimenaoropeza.pillbot.repository

import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.network.RetrofitInstance

class TomaHoyRepository {

    private val api = RetrofitInstance.api

    suspend fun obtenerTomasHoy(
        usuarioId: Int
    ): List<TomaHoy> {
        return api.obtenerTomasHoy(usuarioId)
    }
}