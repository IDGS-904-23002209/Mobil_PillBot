package com.jimenaoropeza.pillbot.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenaoropeza.pillbot.modelo.RegisterRequest
import com.jimenaoropeza.pillbot.repository.AuthRepository
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import retrofit2.HttpException

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    // --- ESTADOS DEL FORMULARIO DE REGISTRO Y SESIÓN ---
    var nombre by mutableStateOf("")
    var apellidoPaterno by mutableStateOf("")
    var apellidoMaterno by mutableStateOf("")
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var telefono by mutableStateOf("")
    var idRol by mutableStateOf(1) // Cambia al ID de rol predeterminado de tu BD

    // --- ESTADOS DE SESIÓN Y UI ---
    var usuarioNombre by mutableStateOf("Usuario")
        private set

    var usuarioId by mutableStateOf(0)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Inicia sesión validando credenciales contra el servidor backend
     */
    fun iniciarSesion(
        correo: String,
        contrasena: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = repository.login(correo, contrasena)

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()

                    if (loginResponse != null && loginResponse.success && loginResponse.data != null) {
                        usuarioNombre = loginResponse.data.nombre.ifBlank { "Usuario" }
                        usuarioId = loginResponse.data.idUsuario

                        onSuccess(usuarioNombre)
                    } else {
                        errorMessage =
                            loginResponse?.message ?: "Credenciales incorrectas o usuario inactivo"
                    }
                } else {
                    errorMessage = "Error en el servidor: ${response.code()}"
                }
            } catch (e: UnknownHostException) {
                errorMessage = "Sin conexión a Internet. Verifica tu red."
            } catch (e: HttpException) {
                errorMessage = "Error en el servidor: ${e.message()}"
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Error en el sistema: ${e.localizedMessage ?: "Fallo inesperado"}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Registra un nuevo usuario
     */
    fun registrarUsuario(onSuccess: () -> Unit) {
        if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank()) {
            errorMessage = "Por favor, completa los campos requeridos."
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    nombre = nombre.trim(),
                    apellidoPaterno = apellidoPaterno.trim(),
                    apellidoMaterno = apellidoMaterno.trim(),
                    correo = correo.trim(),
                    contrasena = contrasena,
                    telefono = if (telefono.isBlank()) "0000000000" else telefono.trim(),
                    idRol = idRol
                )

                val response = repository.registrar(request)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e(
                        "API_ERROR",
                        "Código: ${response.code()} | Detalle: $errorBody"
                    )

                    errorMessage = when (response.code()) {
                        400 -> "Datos de registro inválidos. Revisa la información."
                        409 -> "El correo electrónico ya está registrado."
                        else -> "Error en el servidor (${response.code()})"
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error de red: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}