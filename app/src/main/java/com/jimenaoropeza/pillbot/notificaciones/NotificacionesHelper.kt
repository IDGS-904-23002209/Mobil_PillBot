package com.jimenaoropeza.pillbot.notificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jimenaoropeza.pillbot.MainActivity
import com.jimenaoropeza.pillbot.R

private const val CANAL_TOMAS_ID = "canal_tomas_pendientes"
private const val CANAL_TOMAS_NOMBRE = "Recordatorios de medicamentos"
private const val CANAL_TOMAS_DESCRIPCION = "Notificaciones de tomas de medicamentos pendientes"

const val EXTRA_ABRIR_INICIO = "abrir_inicio"

fun crearCanalNotificacionesTomas(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CANAL_TOMAS_ID, CANAL_TOMAS_NOMBRE, importance).apply {
            description = CANAL_TOMAS_DESCRIPCION
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun notificarTomaPendiente(
    context: Context,
    idProgramacion: Int,
    nombreMedicamento: String,
    dosis: String,
    horaToma: String
) {
    crearCanalNotificacionesTomas(context)

    // Intent que abre (o trae al frente) MainActivity y le indica que navegue a Inicio
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        putExtra(EXTRA_ABRIR_INICIO, true)
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        idProgramacion, // requestCode único por toma para que cada PendingIntent sea distinto
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(context, CANAL_TOMAS_ID)
        .setSmallIcon(R.drawable.ic_pildora)
        .setContentTitle("Toma pendiente: $nombreMedicamento")
        .setContentText("Dosis: $dosis a las $horaToma")
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("Tienes pendiente tomar $nombreMedicamento ($dosis) programado para las $horaToma.")
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notify(idProgramacion, builder.build())
    }
}

fun cancelarNotificacionToma(context: Context, idProgramacion: Int) {
    NotificationManagerCompat.from(context).cancel(idProgramacion)
}