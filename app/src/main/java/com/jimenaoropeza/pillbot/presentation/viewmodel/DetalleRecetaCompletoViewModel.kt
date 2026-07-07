package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.jimenaoropeza.pillbot.data.modelo.DetalleRecetaCompletoRequest
import com.jimenaoropeza.pillbot.data.repository.DetalleRecetaCompletoRepository
import kotlinx.coroutines.launch

class DetalleRecetaCompletoViewModel : ViewModel() {

    private val repository = DetalleRecetaCompletoRepository()

    var detallesCompletos = mutableStateOf<List<DetalleRecetaCompletoRequest>>(emptyList())
    var cargando = mutableStateOf(false)

    // soloDisponibles = true  -> para el formulario de REGISTRAR (excluye ya programados)
    // soloDisponibles = false -> para el formulario de EDITAR (trae todos)
    fun cargarTodosLosDetalles(soloDisponibles: Boolean = true, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            cargando.value = true
            try {
                val lista = repository.obtenerDetallesCompletos(soloDisponibles)
                Log.d("DETALLE_COMPLETO_VM", "Detalles recibidos (soloDisponibles=$soloDisponibles): ${lista.size}")
                detallesCompletos.value = lista
            } catch (e: Exception) {
                Log.e("DETALLE_COMPLETO_VM", "Error al obtener detalles completos", e)
                onError(e.message ?: "Error de conexión al cargar detalle receta")
            } finally {
                cargando.value = false
            }
        }
    }
}