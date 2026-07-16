package com.jimenaoropeza.pillbot.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.data.modelo.HistorialRequest
import com.jimenaoropeza.pillbot.repository.HistorialRepository
import kotlinx.coroutines.launch

class HistorialViewModel : ViewModel() {

    private val repository = HistorialRepository()

    // CORREGIDO: Añadimos un callback opcional 'onSuccess'
    fun marcarTomaComoRealizada(historial: HistorialRequest, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = repository.registrarToma(historial)
                if (response.isSuccessful) {
                    Log.d("PillBot", "Toma registrada exitosamente en la nube")
                    // Ejecutamos el callback en el hilo principal para actualizar la interfaz
                    onSuccess()
                } else {
                    Log.e("PillBot", "Error del servidor: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("PillBot", "Fallo de conexión a la nube", e)
            }
        }
    }
}