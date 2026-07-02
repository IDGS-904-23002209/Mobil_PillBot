package com.jimenaoropeza.pillbot.repository

import com.jimenaoropeza.pillbot.modelo.LoginRequest
import com.jimenaoropeza.pillbot.modelo.RegisterRequest
import com.jimenaoropeza.pillbot.modelo.AuthResponse
import com.jimenaoropeza.pillbot.modelo.LoginResponse
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import retrofit2.Response

class AuthRepository {
    private val api = RetrofitInstance.api

    suspend fun login(
        correo: String,
        contrasena: String
    ): Response<LoginResponse> {
        val request = LoginRequest(
            correo = correo,
            contrasena = contrasena,
            dispositivo = "Android Mobile (Moto G54)",
            ipOrigen = "127.0.0.1",
            detallesNavegador = "PillBot App v1.0"
        )
        // Llamamos directamente a la API usando el request estructurado
        return api.login(request)
    }

    // Función para el registro de usuarios
    suspend fun registrar(
        nombre: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        correo: String,
        contrasena: String,
        telefono: String,
        idRol: Int
    ): Response<AuthResponse> {
        val request = RegisterRequest(
            nombre = nombre,
            apellidoPaterno = apellidoPaterno,
            apellidoMaterno = apellidoMaterno,
            correo = correo,
            contrasena = contrasena,
            telefono = telefono,
            idRol = idRol
        )
        return api.registrar(request)
    }
}