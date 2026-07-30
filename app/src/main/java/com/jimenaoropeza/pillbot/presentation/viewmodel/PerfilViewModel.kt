package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.data.modelo.UsuarioDto
import com.jimenaoropeza.pillbot.network.ApiService
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PerfilState {
    object Loading : PerfilState()
    data class Success(val usuario: UsuarioDto) : PerfilState()
    data class Error(val mensaje: String) : PerfilState()
}

class PerfilViewModel : ViewModel() {

    // Apagamos la propiedad 'api' definida en tu RetrofitInstance
    private val apiService: ApiService = RetrofitInstance.api

    private val _state = MutableStateFlow<PerfilState>(PerfilState.Loading)
    val state: StateFlow<PerfilState> = _state

    fun obtenerPerfil(usuarioId: Int) {
        viewModelScope.launch {
            _state.value = PerfilState.Loading
            try {
                val response = apiService.buscarUsuarioPorId(usuarioId)
                if (response.isSuccessful && response.body()?.success == true) {
                    val usuarioData = response.body()?.data
                    if (usuarioData != null) {
                        _state.value = PerfilState.Success(usuarioData)
                    } else {
                        _state.value = PerfilState.Error("No se encontraron datos del usuario.")
                    }
                } else {
                    _state.value = PerfilState.Error("Error en la respuesta del servidor.")
                }
            } catch (e: Exception) {
                _state.value = PerfilState.Error(e.message ?: "Error de conexión a la red.")
            }
        }
    }
}