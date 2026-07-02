package com.jimenaoropeza.pillbot.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // Instancia del repositorio para respetar el patrón MVVM
    private val repository = AuthRepository()

    var usuarioNombre by mutableStateOf<String>("Usuario")
        private set

    var usuarioId by mutableStateOf<Int>(0)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun iniciarSesion(
        correo: String,
        contrasena: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // Delegamos la lógica de creación del request al repositorio
                val response = repository.login(correo, contrasena)

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()

                    if (loginResponse != null && loginResponse.success && loginResponse.data != null) {
                        // Respaldo por si el backend llega a mandar un campo string vacío o nulo
                        usuarioNombre = loginResponse.data.nombre.ifBlank { "Usuario" }
                        usuarioId = loginResponse.data.idUsuario

                        onSuccess(usuarioNombre)
                    } else {
                        errorMessage = loginResponse?.message ?: "Credenciales incorrectas o usuario inactivo"
                    }
                } else {
                    errorMessage = "Error en el servidor: ${response.code()}"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Error en el sistema: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun registrarUsuario(
        nombre: String,
        correo: String,
        contrasena: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // Ahora usamos correctamente el repositorio en lugar de llamar directo a RetrofitInstance
                val response = repository.registrar(
                    nombre = nombre,
                    apellidoPaterno = "N/A",
                    apellidoMaterno = "N/A",
                    correo = correo,
                    contrasena = contrasena,
                    telefono = "0000000000",
                    idRol = 3
                )
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage = "Error al registrar: El usuario ya existe"
                }
            } catch (e: Exception) {
                errorMessage = "Error de red: Fallo al conectar con el servidor"
            } finally {
                isLoading = false
            }
        }
    }
}