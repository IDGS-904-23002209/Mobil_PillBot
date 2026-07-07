package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.google.gson.Gson
import com.jimenaoropeza.pillbot.data.modelo.DetalleRecetaRequest
import com.jimenaoropeza.pillbot.network.RespuestaServidor
import com.jimenaoropeza.pillbot.data.repository.DetalleRecetaRepository
import kotlinx.coroutines.launch

class DetalleRecetaViewModel : ViewModel() {

    private val repository = DetalleRecetaRepository()

    var detallesReceta = mutableStateOf<List<DetalleRecetaRequest>>(emptyList())
    var cargando = mutableStateOf(false)

    fun cargarDetallesPorReceta(idReceta: Int, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            cargando.value = true
            try {
                val respuesta = repository.consultarDetallesPorReceta(idReceta)

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    detallesReceta.value = respuesta.body()!!
                } else {
                    detallesReceta.value = emptyList()
                    // Mapea el error 404 estructurado que manda tu API cuando no hay registros
                    val errorJson = respuesta.errorBody()?.string()
                    val msg = parsingMensajeError(errorJson) ?: "Error: ${respuesta.code()}"
                    onError(msg)
                }
            } catch (e: Exception) {
                Log.e("DETALLE_RECETA_VM", "Error de red: ${e.message}")
                onError(e.message ?: "Error de conexión")
            } finally {
                cargando.value = false
            }
        }
    }

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