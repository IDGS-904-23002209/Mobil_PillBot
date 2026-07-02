package com.jimenaoropeza.pillbot.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.repository.TomaHoyRepository
import kotlinx.coroutines.launch

class TomaHoyViewModel : ViewModel() {

    private val repository = TomaHoyRepository()

    var tomasHoy = mutableStateOf<List<TomaHoy>>(emptyList())

    fun cargarTomasHoy(usuarioId: Int) {
        viewModelScope.launch {
            try {
                tomasHoy.value = repository.obtenerTomasHoy(usuarioId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}