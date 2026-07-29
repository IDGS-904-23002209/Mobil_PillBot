package com.jimenaoropeza.pillbot.repository

import com.jimenaoropeza.pillbot.modelo.AuthResponse
import com.jimenaoropeza.pillbot.modelo.LoginRequest
import com.jimenaoropeza.pillbot.modelo.LoginResponse
import com.jimenaoropeza.pillbot.modelo.RegisterRequest
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
            dispositivo = "Motorola Moto G54 5G",
            ipOrigen = "10.0.2.2",
            detallesNavegador = "Android Emulator / PillBot App"
        )
        return api.login(request)
    }

    suspend fun registrar(request: RegisterRequest): Response<AuthResponse> {
        return api.registrar(request)
    }
}