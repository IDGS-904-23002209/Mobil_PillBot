package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.data.modelo.ActualizarClienteRequest
import com.jimenaoropeza.pillbot.data.modelo.ClienteConsultaDto
import com.jimenaoropeza.pillbot.network.ApiService
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PerfilState {
    object Loading : PerfilState()
    data class Success(val cliente: ClienteConsultaDto) : PerfilState()
    data class Error(val mensaje: String) : PerfilState()
}

class PerfilViewModel : ViewModel() {

    private val apiService: ApiService = RetrofitInstance.api

    private val _state = MutableStateFlow<PerfilState>(PerfilState.Loading)
    val state: StateFlow<PerfilState> = _state

    private var idClienteReal: Int = 0

    fun obtenerPerfil(usuarioId: Int) {
        viewModelScope.launch {
            _state.value = PerfilState.Loading
            try {
                // Llamada al endpoint correcto de Clientes
                val response = apiService.consultarCliente(usuarioId)

                if (response.isSuccessful && response.body() != null) {
                    val clienteData = response.body()!!
                    idClienteReal = clienteData.idCliente ?: 0
                    _state.value = PerfilState.Success(clienteData)
                } else {
                    _state.value = PerfilState.Error("Error al consultar perfil (${response.code()})")
                }
            } catch (e: Exception) {
                _state.value = PerfilState.Error(e.message ?: "Error de conexión a la red.")
            }
        }
    }

    fun actualizarPerfilDirecto(
        usuarioId: Int,
        nombre: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        telefono: String,
        correo: String,
        direccion: String,
        fechaNacimiento: String,
        tipoSangre: String?,
        alergias: String?,
        contactoEmergencia: String?,
        telefonoEmergencia: String?,
        contrasenaNueva: String?,
        onResultado: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val fechaFormateada = if (fechaNacimiento.contains("T")) {
                    fechaNacimiento
                } else if (fechaNacimiento.isNotBlank()) {
                    "${fechaNacimiento}T00:00:00.000Z"
                } else {
                    "2000-01-01T00:00:00.000Z"
                }

                val request = ActualizarClienteRequest(
                    idCliente = idClienteReal,
                    usuarioId = usuarioId,
                    nombre = nombre,
                    apellidoPaterno = apellidoPaterno,
                    apellidoMaterno = apellidoMaterno,
                    correo = correo,
                    contrasena = if (!contrasenaNueva.isNullOrEmpty()) contrasenaNueva else null,
                    telefono = telefono,
                    fechaNacimiento = fechaFormateada,
                    direccion = direccion,
                    tipoSangre = tipoSangre?.ifBlank { null },
                    alergias = alergias?.ifBlank { null },
                    contactoEmergencia = contactoEmergencia?.ifBlank { null },
                    telefonoEmergencia = telefonoEmergencia?.ifBlank { null }
                )

                val response = apiService.actualizarCliente(request)
                if (response.isSuccessful) {
                    onResultado(true, "Perfil actualizado correctamente")
                } else {
                    onResultado(false, "Error al actualizar (${response.code()})")
                }
            } catch (e: Exception) {
                onResultado(false, e.message ?: "Error de conexión")
            }
        }
    }
}