package com.jimenaoropeza.pillbot.viewmodel

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.modelo.MedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.InventarioMedicamentoRequest // Tu nueva data class
import com.jimenaoropeza.pillbot.repository.MedicamentoRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.util.Log
import com.jimenaoropeza.pillbot.modelo.CatalogoMedicamentoRequest
import com.jimenaoropeza.pillbot.network.ApiService

class MedicamentoViewModel : ViewModel() {

    private val repository = MedicamentoRepository()

    var medicamentos = mutableStateOf<List<Medicamento>>(emptyList())

    // Dentro de MedicamentoViewModel.kt
    var medicamentoSeleccionado by mutableStateOf<Medicamento?>(null)

    // Estado para controlar si el guardado en inventario fue exitoso
    var registroExitoso = mutableStateOf(false)

    fun registrarEnInventario(
        inventarioRequest: InventarioMedicamentoRequest,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val respuesta = repository.registrarInventarioMedicamento(inventarioRequest)

                if (respuesta.isSuccessful) {
                    registroExitoso.value = true
                    onResult(true, respuesta.body()?.mensaje ?: "Registrado correctamente")
                } else {
                    registroExitoso.value = false
                    onResult(false, "Error en el servidor: ${respuesta.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false, "Error de red: ${e.message}")
            }
        }
    }


    fun cargarMedicamentos(usuarioId: Int) {
        // 🛡️ CANDADO: Si el id es -1 o menor, no dispares la petición a la API
        if (usuarioId <= 0) {
            Log.w("MEDICAMENTOS_API", "Intento de cargar medicamentos abortado: ID de usuario inválido ($usuarioId)")
            return
        }

        viewModelScope.launch {
            try {
                val lista = repository.obtenerMedicamentos(usuarioId)
                Log.d("MEDICAMENTOS_API", "Petición realizada para el usuarioId: $usuarioId")
                Log.d("MEDICAMENTOS_API", "Número de registros reales devueltos por la nube: ${lista.size}")
                medicamentos.value = lista
            } catch (e: Exception) {
                Log.e("MEDICAMENTOS_API", "Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun registrarMedicamentoCatalogo(
        request: CatalogoMedicamentoRequest, onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val respuesta = repository.registrarMedicamentoCatalogo(request)
                if (respuesta.isSuccessful) {
                    registroExitoso.value = true
                    onResult(
                        true, respuesta.body()?.mensaje ?: "Medicamento registrado correctamente."
                    )
                } else {
                    registroExitoso.value = false
                    onResult(
                        false, "Error del servidor: ${respuesta.code()}"
                    )
                }
            } catch (e: Exception) {
                registroExitoso.value = false
                onResult(
                    false, e.message ?: "Error desconocido"
                )
            }
        }
    }


    fun guardarMedicamento(medicamento: MedicamentoRequest, onResultado: (Boolean, Int?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.registrarMedicamento(medicamento)
                if (response.isSuccessful && response.body() != null) {
                    val idGenerado = response.body()?.idMedicamento
                    onResultado(true, idGenerado)
                } else {
                    onResultado(false, null)
                }
            } catch (e: Exception) {
                Log.e("MEDICAMENTOS_API", "Error al guardar: ${e.message}")
                onResultado(false, null)
            }
        }
    }

}