package com.jimenaoropeza.pillbot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.modelo.RecordatorioRequest
import com.jimenaoropeza.pillbot.repository.RecordatorioRepository
import kotlinx.coroutines.launch

class RecordatorioViewModel : ViewModel() {

    private val repository = RecordatorioRepository()

    // Mantenemos únicamente tu función original para registrar/guardar medicamentos
    fun guardarRecordatorio(
        recordatorio: RecordatorioRequest,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.registrarRecordatorio(recordatorio)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}