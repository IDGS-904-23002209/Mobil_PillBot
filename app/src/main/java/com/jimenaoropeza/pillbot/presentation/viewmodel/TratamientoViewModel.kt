package com.jimenaoropeza.pillbot.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import com.jimenaoropeza.pillbot.data.modelo.Tratamiento
import kotlinx.coroutines.launch
import com.jimenaoropeza.pillbot.data.repository.TratamientoRepository
import com.jimenaoropeza.pillbot.repository.MedicamentoRepository


class TratamientoViewModel : ViewModel() {

    private val repository = TratamientoRepository()
    var registroExitoso = mutableStateOf(false)

    fun registrarTratamiento(
        tratamiento: Tratamiento,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val respuesta = repository.registrarTratamiento(tratamiento)

                if (respuesta.isSuccessful) {
                    registroExitoso.value = true

                    onResult(
                        true,
                        respuesta.body()?.mensaje ?: "Tratamiento registrado"
                    )
                } else {
                    registroExitoso.value = false

                    onResult(
                        false,
                        "Error ${respuesta.code()}"
                    )
                }

            } catch (e: Exception) {
                registroExitoso.value = false

                onResult(
                    false,
                    e.message ?: "Error"
                )
            }
        }
    }
}