package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.repository.TomaHoyRepository
import com.jimenaoropeza.pillbot.repository.HistorialRepository // Importamos tu Repositorio corregido
import com.jimenaoropeza.pillbot.data.modelo.HistorialRequest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TomaHoyViewModel : ViewModel() {

    private val repository = TomaHoyRepository()
    private val historialRepository = HistorialRepository() // Instancia del repositorio de historial

    private val _tomasHoy = mutableStateOf<List<TomaHoy>>(emptyList())
    val tomasHoy: State<List<TomaHoy>> = _tomasHoy

    private val _estaCargando = mutableStateOf(false)
    val estaCargando: State<Boolean> = _estaCargando

    private val _mensajeError = mutableStateOf<String?>(null)
    val mensajeError: State<String?> = _mensajeError

    fun cargarTomasHoy(usuarioId: Int) {
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeError.value = null
            try {
                val listaDesdeApi = repository.obtenerTomasHoy(usuarioId)
                _tomasHoy.value = listaDesdeApi
            } catch (e: Exception) {
                e.printStackTrace()
                _mensajeError.value = "Error al conectar con el servidor: ${e.localizedMessage}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun marcarComoTomado(usuarioId: Int, idToma: Int) {
        viewModelScope.launch {
            try {
                // 1. Enviamos la actualización real a la base de datos mediante el repositorio
                val exito = repository.marcarMedicamentoTomado(idToma)

                if (exito) {
                    // 2. Si la base de datos se actualizó correctamente, recalculamos la lista de inmediato
                    cargarTomasHoy(usuarioId)
                } else {
                    _mensajeError.value = "No se pudo registrar la toma en el servidor."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _mensajeError.value = "Error de red al marcar como tomado."
            }
        }
    }
}