package com.jimenaoropeza.pillbot.data.repository
import com.jimenaoropeza.pillbot.modelo.CatalogoMedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.modelo.MedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.InventarioMedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.TipoPresentacion
import com.jimenaoropeza.pillbot.data.modelo.Tratamiento
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import com.jimenaoropeza.pillbot.network.RespuestaServidor
import com.jimenaoropeza.pillbot.network.RetrofitInstance.api
import retrofit2.Response

class TratamientoRepository {

    suspend fun registrarTratamiento(
        tratamiento: Tratamiento
    ) = api.registrarTratamiento(tratamiento)
}