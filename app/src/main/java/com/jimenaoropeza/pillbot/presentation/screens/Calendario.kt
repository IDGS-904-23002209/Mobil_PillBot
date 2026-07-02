package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.modelo.HistorialRequest
import com.jimenaoropeza.pillbot.viewmodel.TomaHoyViewModel
import com.jimenaoropeza.pillbot.viewmodel.HistorialViewModel
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.ui.theme.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class CalendarView { DIA, SEMANA, MES, LISTA }

@Composable
fun PillBotCalendarScreen(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit,
    onDiaClick: () -> Unit, // Este callback se activa para saltar al detalle
    viewModel: TomaHoyViewModel = viewModel(),
    historialViewModel: HistorialViewModel = viewModel()
) {
    var selectedView by remember { mutableStateOf(CalendarView.MES) }
    val hoy = LocalDate.now()
    var selectedDay by remember { mutableStateOf(hoy.dayOfMonth) }
    val tomasHoy = viewModel.tomasHoy.value

    LaunchedEffect(Unit) {
        viewModel.cargarTomasHoy(usuarioId = 1)
    }
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF1F1F1),
                tonalElevation = 4.dp
            ) {
                val navIcons = listOf(
                    R.drawable.ic_inicio, R.drawable.ic_formulario, R.drawable.ic_notificacion,
                    R.drawable.ic_inventario, R.drawable.ic_calendario, R.drawable.ic_emergencia, R.drawable.ic_perfil
                )
                val routes = listOf("inicio", "formulario", "notificaciones", "inventario", "calendario", "controlEmergencia", "perfil")

                navIcons.forEachIndexed { index, iconRes ->
                    val isSelected = currentScreen == routes[index]
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onNavTabClick(routes[index]) },
                        icon = {
                            Box(modifier = Modifier.wrapContentSize()) {
                                Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(32.dp))
                                if (index == 2 && totalNoLeidas > 0) {
                                    Box(
                                        modifier = Modifier.size(16.dp).background(Color.Red, shape = RoundedCornerShape(8.dp)).align(Alignment.TopEnd),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = totalNoLeidas.toString(), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = if (isSelected) Color(0xFF59CBA2).copy(alpha = 0.3f) else Color.Transparent)
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Encabezado Corporativo PillBot
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.logopastillero),
                    contentDescription = null,
                    modifier = Modifier.size(55.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "PILLBOT",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Agenda de calendario",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Alarmas resevadas",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.height(50.dp))

            // Selector de Fecha Dinámico
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PillBotLightBlue, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val headerText = if (selectedView == CalendarView.DIA || selectedView == CalendarView.LISTA) "Tomas del día" else "${obtenerNombreMes(hoy.monthValue)} ${hoy.year}"
                Text(text = headerText, fontWeight = FontWeight.Bold, color = PillBotNavy)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de Vista Reactivo (CORREGIDO)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F1F1), RoundedCornerShape(20.dp))
                    .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val viewsMapping = listOf(
                    Triple("Día", Icons.Default.Schedule, CalendarView.DIA),
                    Triple("Semana", Icons.Default.DateRange, CalendarView.SEMANA),
                    Triple("Mes", Icons.Default.ListAlt, CalendarView.MES),
                    Triple("Lista", Icons.Default.List, CalendarView.LISTA)
                )

                // Aquí se corrigió para desestructurar correctamente las 3 variables (label, iconVector, viewType)
                viewsMapping.forEach { (label, iconVector, viewType) ->
                    val isSelected = selectedView == viewType
                    val contentColor = if (isSelected) Color.White else Color.DarkGray

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                            .background(if (isSelected) Color(0xFF59CBA2) else Color.Transparent, RoundedCornerShape(18.dp))
                            .clickable { selectedView = viewType }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Se agregó Row e Icon para renderizar el icono de sistema junto al texto
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = iconVector,
                                contentDescription = null,
                                tint = contentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = label,
                                fontWeight = FontWeight.Bold,
                                color = contentColor,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido dinámico según el filtro
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (selectedView) {
                    CalendarView.MES -> MonthlyGridView(selectedDay = selectedDay, onDaySelected = { day ->
                        selectedDay = day
                        selectedView = CalendarView.LISTA
                    })
                    CalendarView.DIA -> AgendaListView(tomasHoy = tomasHoy, historialViewModel = historialViewModel,onTomaRegistrada = {viewModel.cargarTomasHoy(usuarioId = 1)})
                    CalendarView.SEMANA -> WeeklyView(tomasHoy = tomasHoy, onDayClick = { selectedView = CalendarView.LISTA })
                    CalendarView.LISTA -> AgendaListView(tomasHoy = tomasHoy, historialViewModel = historialViewModel,onTomaRegistrada = {viewModel.cargarTomasHoy(usuarioId = 1)})
                }
            }

            if (selectedView == CalendarView.MES) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(24.dp, 12.dp).background(PillBotNavy))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Alarmas programadas", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PillBotNavy)
                }
            }
        }
    }
}

fun obtenerNombreMes(numeroMes: Int): String {
    return when (numeroMes) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        12 -> "Diciembre"
        else -> ""
    }
}

@Composable
fun MonthlyGridView(selectedDay: Int, onDaySelected: (Int) -> Unit) {
    val hoy = LocalDate.now()
    val diasDelMes = hoy.lengthOfMonth()
    val primerDiaDelMes = hoy.withDayOfMonth(1)
    val espaciosAntes = primerDiaDelMes.dayOfWeek.value - 1

    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight(), colors = CardDefaults.cardColors(containerColor = PillBotCardBackground), elevation = CardDefaults.cardElevation(3.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = listOf("L", "M", "M", "J", "V", "S", "D")
                days.forEach {
                    Text(text = it, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)}
            }
            Spacer(modifier = Modifier.height(8.dp))

            var currentDay = 1
            val totalCeldas = espaciosAntes + diasDelMes
            val totalFilas = if (totalCeldas % 7 == 0) totalCeldas / 7 else (totalCeldas / 7) + 1
            for (row in 0 until totalFilas) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (col in 0 until 7) {
                        val celdaActual = row * 7 + col

                        Box(modifier = Modifier.weight(1f).aspectRatio(1f), contentAlignment = Alignment.Center) {
                            if (celdaActual >= espaciosAntes && currentDay <= diasDelMes) {
                                val dayToShow = currentDay
                                val isSelectedDay = dayToShow == selectedDay
                                val isToday = dayToShow == hoy.dayOfMonth
                                currentDay++

                                Box(modifier = Modifier.size(36.dp).background(if (isSelectedDay) PillBotBlueEvent else Color.Transparent, CircleShape).clickable { onDaySelected(dayToShow) }, contentAlignment = Alignment.Center) {
                                    Text(
                                        text = dayToShow.toString(),
                                        color = if (isSelectedDay) Color.White else PillBotNavy, fontWeight = if (isSelectedDay || isToday) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyView(tomasHoy: List<TomaHoy>, onDayClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        if (tomasHoy.isEmpty()) {
            Text(text = "No hay tomas registradas para hoy.", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(top = 20.dp))
        } else {
            tomasHoy.forEach { toma ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {onDayClick()},
                    colors = CardDefaults.cardColors(
                        containerColor = if (toma.tomado) Color(0xFFF9F9F9) else PillBotLightBlue)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // CORRECCIÓN: Operador elvis para String? seguro
                        Text(text = toma.hora_toma ?: "--:--", fontWeight = FontWeight.Bold, color = PillBotNavy, modifier = Modifier.width(80.dp))

                        Column {
                            // CORRECCIÓN: Operador elvis para String? seguro
                            Text(text = toma.nombre_medicamento ?: "Medicamento sin nombre", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                            Text(text = "Dosis: ${toma.dosis ?: "No especificada"}", fontSize = 12.sp, color = Color.Gray)
                            Text(text = if (toma.tomado) "Tomado" else "Pendiente", fontSize = 12.sp, color = if (toma.tomado) PillBotMint else Color(0xFFFF9800), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AgendaListView(tomasHoy: List<TomaHoy>, historialViewModel: HistorialViewModel, onTomaRegistrada: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Tomas del día",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (tomasHoy.isEmpty()) {
            Text(
                text = "No hay tomas registradas para hoy.",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 20.dp)
            )
        } else {
            tomasHoy.forEach { toma ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PillBotCardBackground
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    if (toma.tomado)
                                        PillBotMint
                                    else
                                        Color(0xFFFF9800),
                                    CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            // CORRECCIÓN: Manejo de nulos seguro al concatenar textos
                            val hora = toma.hora_toma ?: "--:--"
                            val nombre = toma.nombre_medicamento ?: "Medicamento sin nombre"
                            Text(
                                text = "$hora - $nombre",
                                fontWeight = FontWeight.Medium,
                                color = PillBotNavy,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Dosis: ${toma.dosis ?: "No especificada"}",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text =
                                    if (toma.tomado)
                                        "Tomado"
                                    else
                                        "Pendiente",
                                color =
                                    if (toma.tomado)
                                        PillBotMint
                                    else
                                        Color(0xFFFF9800),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (!toma.tomado) {
                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        // CORRECCIÓN: Se cambió id_recordatorio por id_toma que es el real en tu modelo
                                        val historial = HistorialRequest(
                                            id_recordatorio = toma.id_toma,
                                            fecha_registro = LocalDate.now().toString(),
                                            hora_registro = LocalTime.now().format(
                                                DateTimeFormatter.ofPattern("HH:mm")
                                            ),
                                            estatus = "tomado"
                                        )

                                        historialViewModel.registrarToma(
                                            historial = historial,
                                            onSuccess = {
                                                onTomaRegistrada()
                                            }
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PillBotMint
                                    )
                                ) {
                                    Text(
                                        text = "Marcar como tomado",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}