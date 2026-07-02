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

    // Estado reactivo interno encapsulado
    private val _tomasHoy = mutableStateOf<List<TomaHoy>>(emptyList())

    // Estado de lectura para vincular la pantalla de forma segura
    val tomasHoy: State<List<TomaHoy>> = _tomasHoy

    fun cargarTomasHoy(usuarioId: Int) {
        viewModelScope.launch {
            try {
                android.util.Log.d("PILLBOT_DEBUG", "📡 Solicitando tomas para el usuario: $usuarioId")

                val resultado = repository.obtenerTomasHoy(usuarioId)

                android.util.Log.d("PILLBOT_DEBUG", "✅ Servidor respondió. Cantidad de registros recibidos: ${resultado.size}")

                // Imprime cada registro para ver qué valores tienen 'tomado' o si cambian solos
                resultado.forEachIndexed { index, toma ->
                    android.util.Log.d("PILLBOT_DEBUG", "👉 [#$index] Med: ${toma.nombre_medicamento} | Hora: ${toma.hora_toma} | Tomado: ${toma.tomado}")
                }

                _tomasHoy.value = resultado
            } catch (e: Exception) {
                android.util.Log.e("PILLBOT_DEBUG", "❌ Error al cargar tomas de hoy", e)
            }
        }
    }
}