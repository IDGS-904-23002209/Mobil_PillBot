package com.jimenaoropeza.pillbot.repository

import com.jimenaoropeza.pillbot.modelo.RecordatorioRequest
import com.jimenaoropeza.pillbot.network.RetrofitInstance

class RecordatorioRepository {

    private val api = RetrofitInstance.api

    suspend fun registrarRecordatorio(
        recordatorio: RecordatorioRequest
    ): RecordatorioRequest {
        return api.registrarRecordatorio(recordatorio)
    }
}