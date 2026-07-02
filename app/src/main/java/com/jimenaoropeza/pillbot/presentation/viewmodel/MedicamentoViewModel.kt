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

class MedicamentoViewModel : ViewModel() {

    private val repository = MedicamentoRepository()

    var medicamentos = mutableStateOf<List<Medicamento>>(emptyList())

    // Dentro de MedicamentoViewModel.kt
    var medicamentoSeleccionado by mutableStateOf<Medicamento?>(null)

    // Estado para controlar si el guardado en inventario fue exitoso
    var registroExitoso = mutableStateOf(false)


    fun guardarMedicamento(
        medicamento: MedicamentoRequest,
        onSuccess: (Medicamento) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val medicamentoCreado = repository.registrarMedicamento(medicamento)
                cargarMedicamentos(usuarioId = medicamento.id_usuario)
                onSuccess(medicamentoCreado)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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
        viewModelScope.launch {
            try {
                val lista = repository.obtenerMedicamentos(usuarioId)
                Log.d("MEDICAMENTOS_API", lista.toString())
                medicamentos.value = lista
            } catch (e: Exception) {
                Log.e("MEDICAMENTOS_API", "Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}