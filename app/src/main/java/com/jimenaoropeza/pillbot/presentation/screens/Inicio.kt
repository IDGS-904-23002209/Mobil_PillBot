package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.clickable



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimenaoropeza.pillbot.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.presentation.viewmodel.TomaHoyViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.clickable

@Composable
fun Inicio(
    usuarioId: Int,
    currentScreen: String = "inicio",
    onNavTabClick: (String) -> Unit = {},
    onIrANotificaciones: () -> Unit,
    onIrACalendario: () -> Unit,
    onIrATratamiento: () -> Unit,
    onIrAProgramacion: () -> Unit,
    viewModel: TomaHoyViewModel = viewModel(),
    nombreUsuario: String = "Usuario"
) {
    val tomasHoy by viewModel.tomasHoy

    LaunchedEffect(usuarioId) {
        viewModel.cargarTomasHoy(usuarioId = usuarioId)
    }

    val totalTomas = tomasHoy.size
    val tomasTomadas = tomasHoy.count { it.tomado }
    val tomasPendientes = tomasHoy.count { !it.tomado }
    val progreso = if (totalTomas > 0) tomasTomadas.toFloat() / totalTomas.toFloat() else 0f
    val porcentaje = (progreso * 100).toInt()

    val hoy = LocalDate.now(ZoneId.of("America/Mexico_City"))
    val formateador = DateTimeFormatter.ofPattern("dd - MMM - yyyy", Locale("es", "MX"))
    val fechaFormateada = hoy.format(formateador)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Bienvenido, $nombreUsuario",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D2A44)
        )

        Text(
            text = if (tomasPendientes > 0) "Tienes medicamentos pendientes por tomar" else "No tienes medicamentos pendientes",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hoy, $fechaFormateada",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Image(
                painter = painterResource(R.drawable.ic_programacion),
                contentDescription = "Programación",
                modifier = Modifier
                    .size(34.dp)
                    .clickable { onIrAProgramacion() }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$tomasPendientes medicamentos pendientes",
                fontSize = 18.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF99F3BD))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Dosis Tomadas Hoy", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = "$porcentaje%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth().height(10.dp),
                    color = Color(0xFF59CBA2),
                    trackColor = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Lista de horarios",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (tomasHoy.isEmpty()) {
            Text(
                text = "No hay tomas registradas para hoy.",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        } else {
            tomasHoy.forEach { toma ->
                MedicamentItem(
                    toma = toma,
                    onTomarClick = {
                        // Pasamos el ID único de la toma al ViewModel
                        viewModel.marcarComoTomado(usuarioId = usuarioId, idToma = toma.id_toma)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun MedicamentItem(
    toma: TomaHoy,
    onTomarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1D2A44))
                    .padding(vertical = 4.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = toma.hora_toma ?: "--:--",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pildora),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = toma.nombre_medicamento ?: "Medicamento sin nombre",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 14.sp
                        )

                        Text(
                            text = "Dosis: ${toma.dosis ?: "No especificada"}",
                            color = Color.DarkGray,
                            fontSize = 13.sp
                        )

                        Text(
                            text = if (toma.tomado) "Tomado" else "Pendiente",
                            color = if (toma.tomado) Color(0xFF59CBA2) else Color(0xFFFF9800),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Acción Manual: Botón de confirmación
                if (!toma.tomado) {
                    Button(
                        onClick = onTomarClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D2A44)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = "Tomar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Indicador visual de éxito cuando ya se marcó
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFF59CBA2), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✓", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}