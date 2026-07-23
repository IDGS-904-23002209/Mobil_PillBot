package com.jimenaoropeza.pillbot.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.repository.TomaHoyRepository
import com.jimenaoropeza.pillbot.repository.HistorialRepository
import com.jimenaoropeza.pillbot.data.modelo.HistorialRequest
import com.jimenaoropeza.pillbot.notificaciones.notificarTomaPendiente
import com.jimenaoropeza.pillbot.notificaciones.cancelarNotificacionToma
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TomaHoyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TomaHoyRepository()
    private val historialRepository = HistorialRepository()

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

                // Notificamos las pendientes y cancelamos las que ya se tomaron
                val context = getApplication<Application>()
                listaDesdeApi.forEach { toma ->
                    if (!toma.tomado) {
                        notificarTomaPendiente(
                            context = context,
                            idProgramacion = toma.idProgramacion,
                            nombreMedicamento = toma.nombre_medicamento ?: "Medicamento",
                            dosis = toma.dosis ?: "No especificada",
                            horaToma = toma.hora_toma
                        )
                    } else {
                        cancelarNotificacionToma(context, toma.idProgramacion)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _mensajeError.value = "Error al conectar con el servidor: ${e.localizedMessage}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun marcarComoTomado(usuarioId: Int, idToma: Int, idProgramacion: Int, horaProgramada: String) {
        _tomasHoy.value = _tomasHoy.value.map { toma ->
            if (toma.idProgramacion == idProgramacion) toma.copy(estado = true) else toma
        }

        // Cancelamos de inmediato la notificación de esa toma (actualización optimista)
        cancelarNotificacionToma(getApplication(), idProgramacion)

        viewModelScope.launch {
            try {
                val exito = repository.marcarMedicamentoTomado(idToma, idProgramacion, horaProgramada)

                if (exito) {
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