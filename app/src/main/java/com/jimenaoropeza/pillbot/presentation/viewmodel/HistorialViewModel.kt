package com.jimenaoropeza.pillbot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.modelo.HistorialRequest
import com.jimenaoropeza.pillbot.repository.HistorialRepository
import kotlinx.coroutines.launch

class HistorialViewModel : ViewModel() {

    private val repository = HistorialRepository()

    fun registrarToma(
        historial: HistorialRequest,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.registrarToma(historial)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}