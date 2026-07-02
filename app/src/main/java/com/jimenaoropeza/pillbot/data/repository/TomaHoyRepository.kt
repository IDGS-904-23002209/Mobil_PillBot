package com.jimenaoropeza.pillbot.repository

import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.network.ApiService
import com.jimenaoropeza.pillbot.network.RetrofitInstance

class TomaHoyRepository {
    private val apiService: ApiService = RetrofitInstance.api

    suspend fun obtenerTomasHoy(usuarioId: Int): List<TomaHoy> {
        return try {
            apiService.obtenerTomasHoy(usuarioId)
        } catch (e: Exception) {
            emptyList()
        }
    }
}