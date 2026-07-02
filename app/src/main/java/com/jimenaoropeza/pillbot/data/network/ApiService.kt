package com.jimenaoropeza.pillbot.network

import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.modelo.MedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.RecordatorioRequest
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.modelo.HistorialRequest
import com.jimenaoropeza.pillbot.modelo.LoginRequest
import com.jimenaoropeza.pillbot.modelo.RegisterRequest
import com.jimenaoropeza.pillbot.modelo.AuthResponse
import com.jimenaoropeza.pillbot.modelo.CatalogoMedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.LoginResponse
import com.jimenaoropeza.pillbot.modelo.InventarioMedicamentoRequest // 🔥 Asegúrate de tener creada esta data class
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class RespuestaServidor(
    val mensaje: String
)

interface ApiService {

    @GET("api/medicamentos/{usuario_id}")
    suspend fun obtenerMedicamentos(
        @Path("usuario_id") usuarioId: Int
    ): List<Medicamento>

    @POST("api/medicamentos")
    suspend fun registrarMedicamento(
        @Body medicamento: MedicamentoRequest
    ): Medicamento

    @POST("api/admin/inventario-medicamentos")
    suspend fun registrarInventarioMedicamento(
        @Body request: InventarioMedicamentoRequest
    ): Response<RespuestaServidor>

    @POST("api/recordatorios")
    suspend fun registrarRecordatorio(
        @Body recordatorio: RecordatorioRequest
    ): RecordatorioRequest

    @GET("api/recordatorios/hoy/{usuario_id}")
    suspend fun obtenerTomasHoy(
        @Path("usuario_id") usuarioId: Int
    ): List<TomaHoy>

    @POST("api/historial/registrar-toma")
    suspend fun registrarToma(
        @Body historial: HistorialRequest
    ): HistorialRequest

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/auth/registrar")
    suspend fun registrar(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/catalogo/medicamentos")
    suspend fun registrarMedicamentoCatalogo(
        @Body request: CatalogoMedicamentoRequest
    ): Response<RespuestaServidor>
}