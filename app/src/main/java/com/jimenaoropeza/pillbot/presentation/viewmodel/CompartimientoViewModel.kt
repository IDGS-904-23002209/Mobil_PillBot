package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.jimenaoropeza.pillbot.data.modelo.CompartimentoRequest
import com.jimenaoropeza.pillbot.data.repository.CompartimentoRepository
import kotlinx.coroutines.launch

class CompartimentoViewModel : ViewModel() {

    private val repository = CompartimentoRepository()

    var compartimentos = mutableStateOf<List<CompartimentoRequest>>(emptyList())
    var cargando = mutableStateOf(false)

    fun cargarCompartimentos(onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            cargando.value = true
            try {
                val lista = repository.obtenerCompartimentos()
                Log.d("COMPARTIMENTO_VM", "Compartimentos recibidos: ${lista.size}")
                compartimentos.value = lista
            } catch (e: Exception) {
                Log.e("COMPARTIMENTO_VM", "Error al obtener compartimentos", e)
                onError(e.message ?: "Error de conexión al cargar compartimentos")
            } finally {
                cargando.value = false
            }
        }
    }
    fun cargarCompartimentosUsuario(
        idUsuario: Int,
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            cargando.value = true

            try {
                val lista =
                    repository.obtenerCompartimentosUsuario(idUsuario)

                compartimentos.value = lista

                Log.d(
                    "COMPARTIMENTO_VM",
                    "Compartimentos del usuario: ${lista.size}"
                )

            } catch (e: Exception) {

                Log.e(
                    "COMPARTIMENTO_VM",
                    "Error al obtener compartimentos del usuario",
                    e
                )

                onError(
                    e.message
                        ?: "Error al cargar los compartimentos"
                )

            } finally {
                cargando.value = false
            }
        }
    }
}