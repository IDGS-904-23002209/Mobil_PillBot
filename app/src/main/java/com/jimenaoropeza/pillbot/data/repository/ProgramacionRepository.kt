package com.jimenaoropeza.pillbot.data.repository

import com.jimenaoropeza.pillbot.modelo.ProgramacionTratamientoRequest
import com.jimenaoropeza.pillbot.network.RespuestaServidor
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import retrofit2.Response

class ProgramacionRepository {

    private val api = RetrofitInstance.api

    suspend fun registrarProgramacion(
        programacion: ProgramacionTratamientoRequest
    ): Response<RespuestaServidor> {
        return api.registrarProgramacion(programacion)
    }

    suspend fun actualizarProgramacion(
        idProgramacion: Int,
        programacion: ProgramacionTratamientoRequest
    ): Response<RespuestaServidor> {
        return api.actualizarProgramacion(idProgramacion, programacion)
    }

    suspend fun consultarTodos(idUsuario: Int): List<ProgramacionTratamientoRequest> {
        return api.consultarTodos(idUsuario)
    }
}