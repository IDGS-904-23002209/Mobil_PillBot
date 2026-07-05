package com.jimenaoropeza.pillbot.repository

import com.jimenaoropeza.pillbot.modelo.CatalogoMedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.modelo.MedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.InventarioMedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.TipoPresentacion
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import com.jimenaoropeza.pillbot.network.RespuestaServidor // O la clase que maneje el objeto {"mensaje": "..."}
import retrofit2.Response

class MedicamentoRepository {

    private val api = RetrofitInstance.api

    suspend fun obtenerMedicamentos(
        usuarioId: Int
    ): List<Medicamento> {
        return api.obtenerMedicamentos(usuarioId)
    }

    suspend fun registrarMedicamento(
        medicamento: MedicamentoRequest
    ): Response<MedicamentoRequest> {
        return api.registrarMedicamento(medicamento)
    }

    suspend fun registrarInventarioMedicamento(
        inventarioRequest: InventarioMedicamentoRequest
    ): Response<RespuestaServidor> {
        return api.registrarInventarioMedicamento(inventarioRequest)
    }

    suspend fun registrarMedicamentoCatalogo(
        request: CatalogoMedicamentoRequest
    ): Response<RespuestaServidor> {
        return api.registrarMedicamentoCatalogo(request)
    }

    suspend fun obtenerCategorias() =
        api.obtenerCategorias()

    suspend fun obtenerPresentaciones(): List<TipoPresentacion> =
        api.obtenerPresentaciones()

    suspend fun obtenerUnidadesMedida() =
        api.obtenerUnidadesMedida()
}