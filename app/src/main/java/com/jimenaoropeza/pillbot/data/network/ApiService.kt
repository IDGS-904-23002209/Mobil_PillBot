package com.jimenaoropeza.pillbot.network

import com.jimenaoropeza.pillbot.data.modelo.CompartimentoRequest
import com.jimenaoropeza.pillbot.data.modelo.DetalleRecetaCompletoRequest
import com.jimenaoropeza.pillbot.data.modelo.DetalleRecetaRequest
import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.modelo.MedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.RecordatorioRequest
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.data.modelo.HistorialRequest
import com.jimenaoropeza.pillbot.modelo.LoginRequest
import com.jimenaoropeza.pillbot.modelo.RegisterRequest
import com.jimenaoropeza.pillbot.modelo.AuthResponse
import com.jimenaoropeza.pillbot.modelo.CatalogoMedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.Categoria
import com.jimenaoropeza.pillbot.modelo.LoginResponse
import com.jimenaoropeza.pillbot.modelo.InventarioMedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.TipoPresentacion
import com.jimenaoropeza.pillbot.modelo.UnidadMedida
import com.jimenaoropeza.pillbot.data.modelo.Tratamiento
import com.jimenaoropeza.pillbot.modelo.ProgramacionTratamientoRequest

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class RespuestaServidor(
    val mensaje: String,
    val idProgramacion: Int? = null
)

interface ApiService {

    @POST("api/medicamentos/RegistrarMedicamento")
    suspend fun registrarMedicamento(
        @Body medicamento: MedicamentoRequest
    ): Response<MedicamentoRequest>

    @POST("api/recordatorios/RegistrarRecordatorio")
    suspend fun registrarRecordatorio(
        @Body recordatorio: RecordatorioRequest
    ): RecordatorioRequest

    @GET("api/medicamentos/paciente/{usuario_id}")
    suspend fun obtenerMedicamentos(
        @Path("usuario_id") usuarioId: Int
    ): List<Medicamento>

    @POST("api/admin/inventario-medicamentos")
    suspend fun registrarInventarioMedicamento(
        @Body request: InventarioMedicamentoRequest
    ): Response<RespuestaServidor>

    // ENDPOINT CLAVE: Usaremos este método existente con el objeto adecuado para marcar la toma
    @POST("api/historial/registrar-toma")
    suspend fun registrarToma(
        @Body historial: HistorialRequest
    ): Response<Unit> // Cambiado a Response<Unit> para verificar códigos de estado HTTP limpiamente

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

    @GET("api/categorias")
    suspend fun obtenerCategorias(): List<Categoria>

    @GET("api/presentaciones")
    suspend fun obtenerPresentaciones(): List<TipoPresentacion>

    @GET("api/unidadesMedida")
    suspend fun obtenerUnidadesMedida(): List<UnidadMedida>

    @POST("api/tratamientos")
    suspend fun registrarTratamiento(
        @Body tratamiento: Tratamiento
    ): Response<RespuestaServidor>

    @POST("api/programacionTratamientos")
    suspend fun registrarProgramacion(
        @Body programacion: ProgramacionTratamientoRequest
    ): Response<RespuestaServidor>

    @PUT("api/programacionTratamientos/{id_programacion}")
    suspend fun actualizarProgramacion(
        @Path("id_programacion") idProgramacion: Int,
        @Body programacion: ProgramacionTratamientoRequest
    ): Response<RespuestaServidor>

    @GET("api/programacionTratamientos")
    suspend fun consultarTodos(
        @Query("idUsuario") idUsuario: Int
    ): List<ProgramacionTratamientoRequest>

    @GET("api/compartimentos")
    suspend fun obtenerCompartimentos(): List<CompartimentoRequest>

    @GET("api/detalleReceta/receta/{id_receta}")
    suspend fun consultarDetallesPorReceta(
        @Path("id_receta") idReceta: Int
    ): Response<List<DetalleRecetaRequest>>

    @GET("api/detalleReceta/completo")
    suspend fun obtenerDetallesRecetaCompletos(
        @Query("soloDisponibles") soloDisponibles: Boolean = true,
        @Query("idUsuario") idUsuario: Int
    ): List<DetalleRecetaCompletoRequest>

    @GET("api/compartimentos/usuario/{idUsuario}")
    suspend fun obtenerCompartimentosUsuario(
        @Path("idUsuario") idUsuario: Int
    ): List<CompartimentoRequest>

    // Obtener los recordatorios para la pantalla de inicio
    @GET("api/recordatorios/hoy/{usuario_id}")
    suspend fun obtenerTomasHoy(
        @Path("usuario_id") usuarioId: Int
    ): List<TomaHoy>

    // Agregar dentro de la interfaz ApiService existente:

    @PUT("api/compartimentos/{idCompartimento}/cantidad")
    suspend fun actualizarCantidadCompartimento(
        @Path("idCompartimento") idCompartimento: Int,
        @Body request: com.jimenaoropeza.pillbot.data.modelo.ActualizarCantidadRequest
    ): Response<Unit>
}