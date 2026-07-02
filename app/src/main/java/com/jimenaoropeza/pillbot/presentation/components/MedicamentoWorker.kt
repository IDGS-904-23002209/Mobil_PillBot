package com.jimenaoropeza.pillbot.presentation.components

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class MedicamentoWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Recuperamos los datos enviados al worker
        val nombreMed = inputData.getString("MEDICAMENTO_NOMBRE") ?: "tu medicamento"
        val dosis = inputData.getString("MEDICAMENTO_DOSIS") ?: ""

        // Disparamos la notificación al celular
        NotificationHelper.mostrarNotificacion(
            applicationContext,
            "¡Hora de tu medicación! 💊",
            "Recuerda tomar tu dosis de $nombreMed ($dosis)"
        )

        return Result.success()
    }
}