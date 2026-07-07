package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.google.gson.Gson
import com.jimenaoropeza.pillbot.modelo.ProgramacionTratamientoRequest
import com.jimenaoropeza.pillbot.network.RespuestaServidor
import com.jimenaoropeza.pillbot.data.repository.ProgramacionRepository
import kotlinx.coroutines.launch

class ProgramacionViewModel : ViewModel() {

    private val repository = ProgramacionRepository()

    // Estado observable para la UI de Jetpack Compose
    var tratamientos = mutableStateOf<List<ProgramacionTratamientoRequest>>(emptyList())

    var cargando by mutableStateOf(false)

    fun cargarTratamientos() {
        viewModelScope.launch {
            cargando = true
            try {
                val lista = repository.consultarTodos()
                tratamientos.value = lista
            } catch (e: Exception) {
                Log.e("PROGRAMACION_VM", "Error al consultar tratamientos: ${e.message}")
                e.printStackTrace()
            } finally {
                cargando = false
            }
        }
    }

    fun registrarProgramacion(
        programacion: ProgramacionTratamientoRequest,
        onResult: (Boolean, String, Int?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val respuesta = repository.registrarProgramacion(programacion)

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val body = respuesta.body()!!
                    onResult(true, body.mensaje, body.idProgramacion)
                } else {
                    // Extraer el mensaje de error del JSON devuelto en un Bad Request o Conflict
                    val errorJson = respuesta.errorBody()?.string()
                    val mensajeError = parsingMensajeError(errorJson) ?: "Error del servidor: ${respuesta.code()}"
                    onResult(false, mensajeError, null)
                }
            } catch (e: Exception) {
                Log.e("PROGRAMACION_VM", "Error de red al registrar: ${e.message}")
                onResult(false, e.message ?: "Error de red desconocido", null)
            }
        }
    }

    fun actualizarProgramacion(
        idProgramacion: Int,
        programacion: ProgramacionTratamientoRequest,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val respuesta = repository.actualizarProgramacion(idProgramacion, programacion)

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    onResult(true, respuesta.body()!!.mensaje)
                } else {
                    val errorJson = respuesta.errorBody()?.string()
                    val mensajeError = parsingMensajeError(errorJson) ?: "Error al actualizar: ${respuesta.code()}"
                    onResult(false, mensajeError)
                }
            } catch (e: Exception) {
                Log.e("PROGRAMACION_VM", "Error de red al actualizar: ${e.message}")
                onResult(false, e.message ?: "Error de conexión")
            }
        }
    }

    // Helper para deserializar las respuestas de error estructuradas de tu API { "mensaje": "..." }
    private fun parsingMensajeError(jsonStr: String?): String? {
        if (jsonStr.isNullOrEmpty()) return null
        return try {
            val res = Gson().fromJson(jsonStr, RespuestaServidor::class.java)
            res.mensaje
        } catch (e: Exception) {
            null
        }
    }
}