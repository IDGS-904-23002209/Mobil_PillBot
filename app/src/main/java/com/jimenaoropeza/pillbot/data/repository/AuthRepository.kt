package com.jimenaoropeza.pillbot.repository

import com.jimenaoropeza.pillbot.modelo.LoginRequest
import com.jimenaoropeza.pillbot.modelo.RegisterRequest
import com.jimenaoropeza.pillbot.modelo.AuthResponse
import com.jimenaoropeza.pillbot.modelo.LoginResponse
import com.jimenaoropeza.pillbot.network.RetrofitInstance
import retrofit2.Response

class AuthRepository {
    private val api = RetrofitInstance.api

    // Código actual tuyo que está fallando:
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