package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.data.repository.TomaHoyRepository
import kotlinx.coroutines.launch

class TomaHoyViewModel : ViewModel() {

    private val repository = TomaHoyRepository()

    private val _tomasHoy = mutableStateOf<List<TomaHoy>>(emptyList())
    val tomasHoy: State<List<TomaHoy>> = _tomasHoy

    fun cargarTomasHoy(usuarioId: Int) {
        viewModelScope.launch {
            try {
                // Llamamos a tu repositorio que devuelve List<TomaHoy> directamente
                val listaDesdeApi = repository.obtenerTomasHoy(usuarioId)

                if (listaDesdeApi.isNotEmpty()) {
                    // Si el servidor llega a responder con datos reales, se usan
                    _tomasHoy.value = listaDesdeApi
                } else {
                    // SIMULACIÓN: Si viene vacío [], inyectamos datos de prueba limpios
                    _tomasHoy.value = obtenerDatosSimulados()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Si la API truena o no encuentra el host, también cargamos los simulados
                _tomasHoy.value = obtenerDatosSimulados()
            }
        }
    }

    // Usamos estrictamente las variables mapeadas en tu data class TomaHoy
    private fun obtenerDatosSimulados(): List<TomaHoy> {
        return listOf(
            TomaHoy(
                id_toma = 1,
                hora_toma = "08:00 AM",
                nombre_medicamento = "Paracetamol (500 mg)",
                dosis = "1 tableta",
                tomado = true // Barra verde subirá con este
            ),
            TomaHoy(
                id_toma = 2,
                hora_toma = "02:00 PM",
                nombre_medicamento = "Ibuprofeno (400 mg)",
                dosis = "1 cápsula",
                tomado = false
            ),
            TomaHoy(
                id_toma = 3,
                hora_toma = "09:00 PM",
                nombre_medicamento = "Vitamina C (1 g)",
                dosis = "1 tableta efervescente",
                tomado = false
            )
        )
    }
}