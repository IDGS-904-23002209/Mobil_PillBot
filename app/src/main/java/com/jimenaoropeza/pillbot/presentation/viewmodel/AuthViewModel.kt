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

    var nombre by mutableStateOf("")
    var apellidoPaterno by mutableStateOf("")
    var apellidoMaterno by mutableStateOf("")
    var fechaNacimiento by mutableStateOf("")
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var telefono by mutableStateOf("")
    var direccion by mutableStateOf("")
    var idRol by mutableStateOf(3)

    var usuarioNombre by mutableStateOf("Usuario")
        private set

    var usuarioId by mutableStateOf(0)
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
                val response = repository.login(correo, contrasena)

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()

                    if (loginResponse != null && loginResponse.success && loginResponse.data != null) {
                        usuarioNombre = loginResponse.data.nombre.ifBlank { "Usuario" }
                        usuarioId = loginResponse.data.idUsuario
                        onSuccess(usuarioNombre)
                    } else {
                        errorMessage = loginResponse?.message ?: "Credenciales incorrectas o usuario inactivo"
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

    // Registra un nuevo usuario con la estructura completa requerida por el backend
    fun registrarUsuario(onSuccess: () -> Unit) {
        if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank() || direccion.isBlank() || fechaNacimiento.isBlank()) {
            errorMessage = "Por favor, completa los campos requeridos."
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                // Formateamos la fecha a ISO (AAAA-MM-DDTHH:mm:ss.sssZ) para .NET
                val fechaFormatted = if (fechaNacimiento.contains("T")) fechaNacimiento else "${fechaNacimiento}T00:00:00.000Z"

                val request = RegisterRequest(
                    nombre = nombre.trim(),
                    apellidoPaterno = apellidoPaterno.trim(),
                    apellidoMaterno = apellidoMaterno.trim().ifBlank { null },
                    fechaNacimiento = fechaFormatted,
                    correo = correo.trim(),
                    contrasena = contrasena.trim(),
                    telefono = telefono.trim().ifBlank { null },
                    direccion = direccion.trim(),
                    idRol = idRol
                )

                val response = repository.registrar(request)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e("API_ERROR", "Código: ${response.code()} | Detalle: $errorBody")

                    errorMessage = when (response.code()) {
                        400 -> "Datos de registro inválidos. Revisa la información enviada."
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