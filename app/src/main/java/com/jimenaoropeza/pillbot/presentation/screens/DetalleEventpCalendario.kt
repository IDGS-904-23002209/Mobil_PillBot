// pantallas/DetalleEventoCalendario.kt
package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimenaoropeza.pillbot.ui.theme.*

@Composable
fun PillBotEventDetailScreen(
    onVolver: () -> Unit,
    fechaSeleccionada: String = "Lunes, 08 de Junio del 2026",
    eventos: List<EventoDetalle> = eventosEjemplo
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Fila superior con botón de regreso y título
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { onVolver() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar al calendario",
                        modifier = Modifier.size(28.dp),
                        tint = PillBotNavy
                    )
                }

                Text(
                    text = "Eventos por día",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PillBotNavy
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha seleccionada
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PillBotLightBlue, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(fechaSeleccionada, fontWeight = FontWeight.Bold, color = PillBotNavy)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de búsqueda
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PillBotLightBlue.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔍", modifier = Modifier.padding(end = 8.dp))
                Text("Buscador", color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Categorías de la Tabla
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(65.dp))
                DetailCategoryChip(text = "Recetas", color = PillBotMint, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(4.dp))
                DetailCategoryChip(text = "Vitaminas y\nsuplementos", color = PillBotYellow, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(4.dp))
                DetailCategoryChip(text = "Según sea\nnecesario", color = PillBotBlueEvent, textColor = Color.White, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cuerpo de la Agenda con scroll
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                // Columna de horas
                Column(
                    modifier = Modifier
                        .width(65.dp)
                        .fillMaxHeight()
                        .background(PillBotMint.copy(alpha = 0.25f), RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                ) {
                    val hours = listOf("9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM")
                    hours.forEach { hour ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .padding(start = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text = hour, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PillBotNavy)
                        }
                    }
                }

                // Contenido de eventos - Dinámico según eventos recibidos
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (eventos.isEmpty()) {
                        // Mensaje cuando no hay eventos
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "📅",
                                    fontSize = 48.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No hay eventos para este día",
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Toca un día en el calendario para ver los eventos",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    } else {
                        // Agrupar eventos por hora
                        val eventosPorHora = eventos.groupBy { it.hora }

                        horasDisponibles.forEach { hora ->
                            val eventosEnHora = eventosPorHora[hora] ?: emptyList()

                            DetailTableRow {
                                if (eventosEnHora.isNotEmpty()) {
                                    // Mostrar eventos de esta hora
                                    eventosEnHora.forEachIndexed { index, evento ->
                                        DetailEventCard(
                                            title = evento.titulo,
                                            color = evento.color,
                                            textColor = evento.textColor,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (index < eventosEnHora.size - 1) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                    }
                                    // Rellenar espacios vacíos
                                    val espaciosVacios = 3 - eventosEnHora.size
                                    repeat(espaciosVacios) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        if (it < espaciosVacios - 1) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                    }
                                } else {
                                    // Hora sin eventos
                                    Spacer(modifier = Modifier.weight(1f))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Spacer(modifier = Modifier.weight(1f))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }

        // Alerta Flotante (solo si hay eventos pendientes)
        if (eventos.any { it.pendiente }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .align(Alignment.BottomCenter)
                    .background(PillBotMint.copy(alpha = 0.95f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
                    .offset(y = (-20).dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("", fontSize = 32.sp, modifier = Modifier.padding(end = 12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = eventos.filter { it.pendiente }.joinToString(" | ") {
                                "${it.titulo.take(20)}${if (it.titulo.length > 20) "..." else ""}"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PillBotNavy
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .background(Color.White.copy(alpha = 0.4f), CircleShape)
                        ) {
                            val porcentaje = eventos.count { it.tomado }.toFloat() / eventos.size.toFloat() * 100
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(porcentaje / 100f)
                                    .fillMaxHeight()
                                    .background(Color.White, CircleShape),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text("${porcentaje.toInt()}%  ", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PillBotNavy)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// DATA CLASS
// ============================================================
data class EventoDetalle(
    val hora: String,
    val titulo: String,
    val color: Color,
    val textColor: Color = PillBotNavy,
    val tomado: Boolean = false,
    val pendiente: Boolean = true
)

// ============================================================
// DATOS DE EJEMPLO
// ============================================================
val horasDisponibles = listOf("9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM")

val eventosEjemplo = listOf(
    // 9 AM
    EventoDetalle("9 AM", "Dosis vencida/pendiente: Chem-Call 1:50ml", PillBotMint),
    EventoDetalle("9 AM", "Pepto bismol", PillBotBlueEvent, Color.White),
    // 10 AM
    EventoDetalle("10 AM", "Dosis vencida/pendiente: Amoxicilina", PillBotMint),
    // 11 AM
    EventoDetalle("11 AM", "Vitamina A - Dosis: 1/1", PillBotYellow),
    EventoDetalle("11 AM", "Vitamina B - Dosis: 2/10", PillBotYellow),
    EventoDetalle("11 AM", "Vitamina E - Dosis: 1/23", PillBotYellow),
    // 12 PM
    EventoDetalle("12 PM", "Paracetamol 500mg", PillBotMint, tomado = true, pendiente = false),
    EventoDetalle("12 PM", "Ibuprofeno 400mg", PillBotMint),
    // 1 PM
    EventoDetalle("1 PM", "Omeprazol 20mg", PillBotMint),
    EventoDetalle("1 PM", "Pomada de la campana", PillBotBlueEvent, Color.White),
    // 2 PM
    EventoDetalle("2 PM", "Losartán 50mg", PillBotMint),
    EventoDetalle("2 PM", "Metformina 850mg", PillBotMint),
    // 4 PM
    EventoDetalle("4 PM", "Vitamina C - Dosis: 2/10", PillBotYellow),
    EventoDetalle("4 PM", "Vitamina D - Dosis: 1/1", PillBotYellow),
    EventoDetalle("4 PM", "Calcio 500mg", PillBotMint),
    // 5 PM
    EventoDetalle("5 PM", "Magnesio 400mg", PillBotMint),
    // 6 PM
    EventoDetalle("6 PM", "Vitamina E - Dosis: 1/23", PillBotYellow),
    EventoDetalle("6 PM", "Pomada de la campana", PillBotBlueEvent, Color.White),
    // 7 PM
    EventoDetalle("7 PM", "Losartán 50mg", PillBotMint, tomado = true, pendiente = false),
    EventoDetalle("7 PM", "Metformina 850mg", PillBotMint),
    // 8 PM
    EventoDetalle("8 PM", "Paracetamol 500mg", PillBotMint),
    EventoDetalle("8 PM", "Pepto bismol", PillBotBlueEvent, Color.White)
)

// ============================================================
// COMPONENTES REUTILIZABLES
// ============================================================

@Composable
fun DetailCategoryChip(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    textColor: Color = PillBotNavy
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier.padding(horizontal = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 10.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DetailTableRow(content: @Composable RowScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(start = 6.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            content()
        }
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Composable
fun DetailEventCard(
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    textColor: Color = PillBotNavy
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 2.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Text(
            text = title,
            fontSize = 9.sp,
            lineHeight = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(6.dp)
        )
    }
}