
package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jimenaoropeza.pillbot.data.modelo.HistorialRequest
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.presentation.viewmodel.TomaHoyViewModel
import com.jimenaoropeza.pillbot.ui.theme.*
import com.jimenaoropeza.pillbot.viewmodel.HistorialViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

enum class CalendarView { DIA, SEMANA, MES, LISTA }

val datosEjemploHoy = listOf(
    TomaHoy(1, "08:00", "Paracetamol", "500mg", "Tomado"),
    TomaHoy(2, "10:30", "Ibuprofeno", "400mg", "Pendiente"),
    TomaHoy(3, "14:00", "Omeprazol", "20mg", "Tomado"),
    TomaHoy(4, "20:00", "Losartán", "50mg", "Pendiente"),
    TomaHoy(5, "22:00", "Metformina", "850mg", "Pendiente")
)

@Composable
fun PillBotCalendarScreen(
    usuarioId: Int,
    onVolver: () -> Unit,
    navController: NavHostController? = null,
    viewModel: TomaHoyViewModel = viewModel(),
    historialViewModel: HistorialViewModel = viewModel()
) {
    var selectedView by remember { mutableStateOf(CalendarView.MES) }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDay by remember { mutableStateOf(currentDate.dayOfMonth) }
    val tomasReales = viewModel.tomasHoy.value
    val hoy = LocalDate.now()

    val tomasHoy = if (tomasReales.isEmpty() && currentDate == hoy) {
        datosEjemploHoy
    } else {
        tomasReales
    }

    var expandedYear by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }
    val years = (2000..2030).toList()
    val months = (1..12).map {
        LocalDate.of(2000, it, 1).month.getDisplayName(TextStyle.FULL, Locale("es"))
    }

    LaunchedEffect(usuarioId) {
        viewModel.cargarTomasHoy(usuarioId = usuarioId)
    }

    val tomasTomadas = tomasHoy.count { it.tomado }
    val tomasPendientes = tomasHoy.count { !it.tomado }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Agenda de calendario",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PillBotLightBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedMonth = true }
                            .background(Color.Transparent, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentDate.month.getDisplayName(TextStyle.FULL, Locale("es")),
                            fontWeight = FontWeight.Bold,
                            color = PillBotNavy,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (expandedMonth) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = PillBotNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expandedMonth,
                        onDismissRequest = { expandedMonth = false },
                        modifier = Modifier.height(200.dp)
                    ) {
                        months.forEachIndexed { index, monthName ->
                            val monthNumber = index + 1
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = monthName,
                                        fontWeight = if (monthNumber == currentDate.monthValue) FontWeight.Bold else FontWeight.Normal,
                                        color = if (monthNumber == currentDate.monthValue) PillBotMint else Color.Black
                                    )
                                },
                                onClick = {
                                    currentDate = currentDate.withMonth(monthNumber)
                                    expandedMonth = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(modifier = Modifier.weight(0.8f)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedYear = true }
                            .background(Color.Transparent, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentDate.year.toString(),
                            fontWeight = FontWeight.Bold,
                            color = PillBotNavy,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (expandedYear) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = PillBotNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expandedYear,
                        onDismissRequest = { expandedYear = false },
                        modifier = Modifier.height(150.dp)
                    ) {
                        years.forEach { year ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = year.toString(),
                                        fontWeight = if (year == currentDate.year) FontWeight.Bold else FontWeight.Normal,
                                        color = if (year == currentDate.year) PillBotMint else Color.Black
                                    )
                                },
                                onClick = {
                                    currentDate = currentDate.withYear(year)
                                    expandedYear = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1), RoundedCornerShape(24.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val views = listOf(
                Triple("Día", Icons.Default.Schedule, CalendarView.DIA),
                Triple("Semana", Icons.Default.DateRange, CalendarView.SEMANA),
                Triple("Mes", Icons.Default.CalendarMonth, CalendarView.MES),
                Triple("Lista", Icons.Default.List, CalendarView.LISTA)
            )

            views.forEach { view ->
                val label = view.first
                val icon = view.second
                val viewType = view.third
                val isSelected = selectedView == viewType

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isSelected) PillBotMint else Color.Transparent,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedView = viewType }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color.DarkGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedView) {
                CalendarView.DIA -> DayView(
                    currentDate = currentDate,
                    tomasHoy = tomasHoy,
                    historialViewModel = historialViewModel,
                    onTomaRegistrada = { viewModel.cargarTomasHoy(usuarioId = usuarioId) },
                    onDayChanged = { newDate ->
                        currentDate = newDate
                    },
                    navController = navController
                )
                CalendarView.SEMANA -> WeekView(
                    currentDate = currentDate,
                    tomasHoy = tomasHoy,
                    onDayClick = { day ->
                        selectedDay = day
                        currentDate = currentDate.withDayOfMonth(day)
                        selectedView = CalendarView.DIA
                    }
                )
                CalendarView.MES -> MonthView(
                    currentDate = currentDate,
                    selectedDay = selectedDay,
                    tomasHoy = tomasHoy,
                    onDaySelected = { day ->
                        selectedDay = day
                        currentDate = currentDate.withDayOfMonth(day)
                        selectedView = CalendarView.DIA
                    },
                    navController = navController
                )
                CalendarView.LISTA -> ListView(
                    currentDate = currentDate,
                    tomasHoy = tomasHoy,
                    historialViewModel = historialViewModel,
                    onTomaRegistrada = { viewModel.cargarTomasHoy(usuarioId = usuarioId) }
                )
            }
        }
    }
}

@Composable
fun DayView(
    currentDate: LocalDate,
    tomasHoy: List<TomaHoy>,
    historialViewModel: HistorialViewModel,
    onTomaRegistrada: () -> Unit,
    onDayChanged: (LocalDate) -> Unit,
    navController: NavHostController? = null
) {
    val esHoy = currentDate == LocalDate.now()
    var expandedDay by remember { mutableStateOf(false) }
    val daysInMonth = (1..currentDate.lengthOfMonth()).toList()

    val tomasMostrar = if (esHoy && tomasHoy.isEmpty()) {
        datosEjemploHoy
    } else {
        tomasHoy
    }

    // Horas con formato estandarizado de dos dígitos para coincidir con la base de datos
    val horas = listOf(
        "00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
        "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
        "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
        "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (tomasMostrar.isNotEmpty()) {
                        navController?.navigate("detalleEvento")
                    }
                },
            colors = CardDefaults.cardColors(containerColor = if (esHoy) PillBotMint.copy(alpha = 0.15f) else PillBotLightBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onDayChanged(currentDate.minusDays(1)) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Día anterior", modifier = Modifier.size(20.dp))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es")).uppercase(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PillBotNavy
                        )

                        Box(modifier = Modifier.width(80.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expandedDay = true }
                                    .background(Color.Transparent, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentDate.dayOfMonth.toString(),
                                    fontWeight = FontWeight.Bold,
                                    color = PillBotNavy,
                                    fontSize = 20.sp
                                )
                                Icon(
                                    imageVector = if (expandedDay) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = PillBotNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = expandedDay,
                                onDismissRequest = { expandedDay = false },
                                modifier = Modifier.height(150.dp)
                            ) {
                                daysInMonth.forEach { day ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = day.toString(),
                                                fontWeight = if (day == currentDate.dayOfMonth) FontWeight.Bold else FontWeight.Normal,
                                                color = if (day == currentDate.dayOfMonth) PillBotMint else Color.Black
                                            )
                                        },
                                        onClick = {
                                            onDayChanged(currentDate.withDayOfMonth(day))
                                            expandedDay = false
                                        }
                                    )
                                }
                            }
                        }

                        Text(
                            text = "${currentDate.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentDate.year}",
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }

                    IconButton(
                        onClick = { onDayChanged(currentDate.plusDays(1)) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Día siguiente", modifier = Modifier.size(20.dp))
                    }
                }

                if (esHoy) {
                    Text(
                        text = "Hoy",
                        fontSize = 12.sp,
                        color = PillBotMint,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val tomados = tomasMostrar.count { it.tomado }
            val pendientes = tomasMostrar.count { !it.tomado }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(PillBotMint, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tomados: $tomados", fontSize = 12.sp, color = PillBotMint)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(Color(0xFFFF9800), CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Pendientes: $pendientes", fontSize = 12.sp, color = Color(0xFFFF9800))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Total: ${tomasMostrar.size}", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Tomas del día",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        horas.forEach { hora ->
            // Se filtran las tomas comparando únicamente las primeras dos cifras (las horas)
            val tomasEnHora = tomasMostrar.filter { toma ->
                val horaToma = toma.hora_toma ?: ""
                if (horaToma.length >= 2) {
                    horaToma.substring(0, 2) == hora.substring(0, 2)
                } else {
                    false
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        if (tomasEnHora.isNotEmpty()) {
                            navController?.navigate("detalleEvento")
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (tomasEnHora.isNotEmpty()) PillBotLightBlue else Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
                elevation = if (tomasEnHora.isNotEmpty()) CardDefaults.cardElevation(2.dp) else CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = hora,
                        fontWeight = FontWeight.Medium,
                        color = if (tomasEnHora.isNotEmpty()) PillBotNavy else Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.width(55.dp)
                    )

                    if (tomasEnHora.isNotEmpty()) {
                        Column(modifier = Modifier.weight(1f)) {
                            tomasEnHora.forEachIndexed { index, toma ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(
                                                if (toma.tomado) PillBotMint else Color(0xFFFF9800),
                                                CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = toma.nombre_medicamento ?: "Sin nombre",
                                        fontSize = 13.sp,
                                        color = PillBotNavy,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    // Corregido: Uso de cantidadDosis para evitar errores de compilación
                                    Text(
                                        text = "(${toma.dosis ?: "${toma.dosis ?: 1} dosis"})",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                                if (index < tomasEnHora.size - 1) {
                                    Divider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        color = Color.Gray.copy(alpha = 0.2f)
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Sin alarmas",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun WeekView(
    currentDate: LocalDate,
    tomasHoy: List<TomaHoy>,
    onDayClick: (Int) -> Unit
) {
    val yearMonth = YearMonth.from(currentDate)
    val primerDiaDelMes = currentDate.withDayOfMonth(1)
    val ultimoDiaDelMes = yearMonth.atEndOfMonth()

    val inicioPrimeraSemana = primerDiaDelMes.minusDays((primerDiaDelMes.dayOfWeek.value - 1).toLong())
    val finUltimaSemana = ultimoDiaDelMes.plusDays((7 - ultimoDiaDelMes.dayOfWeek.value).toLong())

    var semanaInicio = inicioPrimeraSemana
    val semanas = mutableListOf<List<LocalDate>>()

    while (semanaInicio <= finUltimaSemana) {
        val semana = (0..6).map { semanaInicio.plusDays(it.toLong()) }
        semanas.add(semana)
        semanaInicio = semanaInicio.plusWeeks(1)
    }

    val hoy = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PillBotLightBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "${currentDate.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentDate.year}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PillBotNavy,
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val tomasDelDia = tomasHoy
            val tomados = tomasDelDia.count { it.tomado }
            val pendientes = tomasDelDia.count { !it.tomado }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(PillBotMint, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tomados: $tomados", fontSize = 12.sp, color = PillBotMint)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(Color(0xFFFF9800), CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Pendientes: $pendientes", fontSize = 12.sp, color = Color(0xFFFF9800))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Total: ${tomasDelDia.size}", fontSize = 12.sp, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        semanas.forEachIndexed { index, semana ->
            val inicio = semana.first()
            val fin = semana.last()
            val esSemanaActual = inicio <= hoy && fin >= hoy

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        val diaClick = if (esSemanaActual) hoy.dayOfMonth else inicio.dayOfMonth
                        onDayClick(diaClick)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (esSemanaActual) PillBotMint.copy(alpha = 0.1f) else PillBotCardBackground
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${inicio.dayOfMonth}-${fin.dayOfMonth} de ${inicio.month.getDisplayName(TextStyle.FULL, Locale("es"))}",
                            fontSize = 13.sp,
                            fontWeight = if (esSemanaActual) FontWeight.Bold else FontWeight.Medium,
                            color = if (esSemanaActual) PillBotMint else Color.DarkGray
                        )
                        if (esSemanaActual) {
                            Text(
                                text = "Esta semana",
                                fontSize = 11.sp,
                                color = PillBotMint,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        semana.forEach { fecha ->
                            val esHoy = fecha == hoy
                            val esDelMes = fecha.month == currentDate.month

                            val tomasDelDia = if (esHoy && tomasHoy.isEmpty()) {
                                datosEjemploHoy
                            } else if (esHoy) {
                                tomasHoy
                            } else {
                                emptyList()
                            }
                            val tomados = tomasDelDia.count { it.tomado }
                            val pendientes = tomasDelDia.count { !it.tomado }
                            val tieneTomas = tomasDelDia.isNotEmpty()

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (esDelMes) {
                                            onDayClick(fecha.dayOfMonth)
                                        }
                                    }
                            ) {
                                Text(
                                    text = fecha.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es")).uppercase(),
                                    fontSize = 9.sp,
                                    color = if (esHoy) PillBotMint else if (esDelMes) Color.Gray else Color.LightGray,
                                    fontWeight = if (esHoy) FontWeight.Bold else FontWeight.Normal
                                )

                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            when {
                                                esHoy && tieneTomas -> Color.Transparent
                                                esHoy -> PillBotMint.copy(alpha = 0.3f)
                                                !esDelMes -> Color.Transparent
                                                tieneTomas -> PillBotNavy.copy(alpha = 0.15f)
                                                else -> Color.Transparent
                                            },
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = fecha.dayOfMonth.toString(),
                                        fontSize = 13.sp,
                                        fontWeight = if (esHoy || tieneTomas) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            !esDelMes -> Color.LightGray
                                            esHoy -> PillBotMint
                                            tieneTomas -> PillBotNavy
                                            else -> Color.DarkGray
                                        }
                                    )
                                }

                                if (tieneTomas && esDelMes) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.height(8.dp)
                                    ) {
                                        if (tomados > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .background(PillBotMint, CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                        }
                                        if (pendientes > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .background(Color(0xFFFF9800), CircleShape)
                                            )
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.height(8.dp))
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
fun MonthView(
    currentDate: LocalDate,
    selectedDay: Int,
    tomasHoy: List<TomaHoy>,
    onDaySelected: (Int) -> Unit,
    navController: NavHostController? = null
) {
    val yearMonth = YearMonth.from(currentDate)
    val diasDelMes = yearMonth.lengthOfMonth()
    val primerDiaDelMes = currentDate.withDayOfMonth(1)
    val espaciosAntes = if (primerDiaDelMes.dayOfWeek.value == 7) 0 else primerDiaDelMes.dayOfWeek.value
    val hoy = LocalDate.now()

    val tieneTomasHoy = tomasHoy.isNotEmpty()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val tomasDelDia = if (tomasHoy.isEmpty() && currentDate == hoy) datosEjemploHoy else tomasHoy
            val tomados = tomasDelDia.count { it.tomado }
            val pendientes = tomasDelDia.count { !it.tomado }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(PillBotMint, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tomados: $tomados", fontSize = 12.sp, color = PillBotMint)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(Color(0xFFFF9800), CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Pendientes: $pendientes", fontSize = 12.sp, color = Color(0xFFFF9800))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Total: ${tomasDelDia.size}", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = PillBotCardBackground),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val days = listOf("L", "M", "M", "J", "V", "S", "D")
                    days.forEach { day ->
                        Text(
                            text = day,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                var currentDay = 1
                val totalCeldas = espaciosAntes + diasDelMes
                val totalFilas = if (totalCeldas % 7 == 0) totalCeldas / 7 else (totalCeldas / 7) + 1

                for (row in 0 until totalFilas) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0 until 7) {
                            val celdaActual = row * 7 + col

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                if (celdaActual >= espaciosAntes && currentDay <= diasDelMes) {
                                    val dayToShow = currentDay
                                    val esHoy = dayToShow == hoy.dayOfMonth &&
                                            currentDate.month == hoy.month &&
                                            currentDate.year == hoy.year
                                    val isSelected = dayToShow == selectedDay

                                    val tieneTomas = esHoy && tieneTomasHoy

                                    currentDay++

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .background(
                                                    when {
                                                        isSelected -> PillBotMint
                                                        esHoy && tieneTomas -> PillBotMint.copy(alpha = 0.2f)
                                                        esHoy -> Color(0xFFE8F5E9)
                                                        tieneTomas -> PillBotNavy.copy(alpha = 0.15f)
                                                        else -> Color.Transparent
                                                    },
                                                    CircleShape
                                                )
                                                .clickable {
                                                    onDaySelected(dayToShow)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = dayToShow.toString(),
                                                color = when {
                                                    isSelected -> Color.White
                                                    esHoy -> PillBotMint
                                                    tieneTomas -> PillBotNavy
                                                    else -> Color.DarkGray
                                                },
                                                fontWeight = if (isSelected || esHoy || tieneTomas) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 14.sp
                                            )
                                        }

                                        if (tieneTomas && !isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .size(5.dp)
                                                    .background(PillBotNavy, CircleShape)
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
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
fun ListView(
    currentDate: LocalDate,
    tomasHoy: List<TomaHoy>,
    historialViewModel: HistorialViewModel,
    onTomaRegistrada: () -> Unit
) {
    val yearMonth = YearMonth.from(currentDate)
    val primerDiaDelMes = currentDate.withDayOfMonth(1)
    val ultimoDiaDelMes = yearMonth.atEndOfMonth()

    val inicioPrimeraSemana = primerDiaDelMes.minusDays((primerDiaDelMes.dayOfWeek.value - 1).toLong())
    val finUltimaSemana = ultimoDiaDelMes.plusDays((7 - ultimoDiaDelMes.dayOfWeek.value).toLong())

    var semanaInicio = inicioPrimeraSemana
    val semanas = mutableListOf<List<LocalDate>>()

    while (semanaInicio <= finUltimaSemana) {
        val semana = (0..6).map { semanaInicio.plusDays(it.toLong()) }
        semanas.add(semana)
        semanaInicio = semanaInicio.plusWeeks(1)
    }

    val hoy = LocalDate.now()
    var expandedWeeks by remember { mutableStateOf(setOf<Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Agenda",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PillBotNavy,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val tomasSemanaActual = if (tomasHoy.isEmpty() && currentDate == hoy) datosEjemploHoy else tomasHoy
        val tomadosSemana = tomasSemanaActual.count { it.tomado }
        val pendientesSemana = tomasSemanaActual.count { !it.tomado }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(PillBotMint, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tomados: $tomadosSemana", fontSize = 12.sp, color = PillBotMint)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(Color(0xFFFF9800), CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Pendientes: $pendientesSemana", fontSize = 12.sp, color = Color(0xFFFF9800))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Total: ${tomasSemanaActual.size}", fontSize = 12.sp, color = Color.Gray)
            }
        }

        semanas.forEachIndexed { index, semana ->
            val inicio = semana.first()
            val fin = semana.last()
            val esSemanaActual = inicio <= hoy && fin >= hoy
            val isExpanded = expandedWeeks.contains(index)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.cardElevation(0.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isExpanded) {
                                expandedWeeks = expandedWeeks - index
                            } else {
                                expandedWeeks = expandedWeeks + index
                            }
                        }
                        .padding(12.dp)
                        .background(
                            if (esSemanaActual) PillBotMint.copy(alpha = 0.1f) else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${inicio.dayOfMonth}-${fin.dayOfMonth} de ${inicio.month.getDisplayName(TextStyle.FULL, Locale("es"))}",
                            fontSize = 14.sp,
                            fontWeight = if (esSemanaActual) FontWeight.Bold else FontWeight.Medium,
                            color = if (esSemanaActual) PillBotMint else Color.DarkGray
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (esSemanaActual) {
                                Text(
                                    text = "Esta semana",
                                    fontSize = 11.sp,
                                    color = PillBotMint,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        semana.forEach { fecha ->
                            val esHoy = fecha == hoy
                            val esDelMes = fecha.month == currentDate.month

                            val tomasDelDia = if (esHoy && tomasHoy.isEmpty()) {
                                datosEjemploHoy
                            } else if (esHoy) {
                                tomasHoy
                            } else {
                                emptyList()
                            }
                            val tomados = tomasDelDia.count { it.tomado }
                            val pendientes = tomasDelDia.count { !it.tomado }
                            val tieneTomas = tomasDelDia.isNotEmpty()

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = fecha.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es")).uppercase(),
                                    fontSize = 9.sp,
                                    color = if (esHoy) PillBotMint else if (esDelMes) Color.Gray else Color.LightGray
                                )
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(
                                            when {
                                                esHoy && tieneTomas -> Color.Transparent
                                                esHoy -> PillBotMint.copy(alpha = 0.3f)
                                                !esDelMes -> Color.Transparent
                                                tieneTomas -> PillBotNavy.copy(alpha = 0.15f)
                                                else -> Color.Transparent
                                            },
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = fecha.dayOfMonth.toString(),
                                        fontSize = 12.sp,
                                        fontWeight = if (esHoy || tieneTomas) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            !esDelMes -> Color.LightGray
                                            esHoy -> PillBotMint
                                            tieneTomas -> PillBotNavy
                                            else -> Color.DarkGray
                                        }
                                    )
                                }
                                if (tieneTomas && esDelMes) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.height(6.dp)
                                    ) {
                                        if (tomados > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .background(PillBotMint, CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                        }
                                        if (pendientes > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .background(Color(0xFFFF9800), CircleShape)
                                            )
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                            }
                        }
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(12.dp))

                        val tomasAMostrar = if (tomasHoy.isEmpty() && esSemanaActual) {
                            datosEjemploHoy
                        } else if (esSemanaActual) {
                            tomasHoy
                        } else {
                            emptyList()
                        }

                        if (tomasAMostrar.isEmpty()) {
                            Text(
                                text = "No tienes alarmas esta semana",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(8.dp)
                            )
                        } else {
                            tomasAMostrar.forEach { toma ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(
                                            if (toma.tomado) PillBotMint.copy(alpha = 0.1f) else Color(0xFFFF9800).copy(alpha = 0.1f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(
                                                if (toma.tomado) PillBotMint else Color(0xFFFF9800),
                                                CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${toma.hora_toma ?: "--:--"} - ${toma.nombre_medicamento ?: "Sin nombre"}",
                                            fontSize = 13.sp,
                                            color = PillBotNavy
                                        )
                                        Text(
                                            text = "Dosis: ${toma.dosis ?: "No especificada"}",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    if (!toma.tomado) {
                                        Button(
                                            onClick = {
                                                // CORREGIDO: Nombres de variables alineados al modelo HistorialRequest
                                                val historial = HistorialRequest(
                                                    idToma = toma.id_toma,
                                                    fechaReal = LocalDate.now().toString() + "Z", // Formato ISO básico
                                                    horaReal = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                                    estatus = "Tomado"
                                                )
                                                historialViewModel.marcarTomaComoRealizada(
                                                    historial = historial,
                                                    onSuccess = onTomaRegistrada
                                                )
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = PillBotMint
                                            ),
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                                            modifier = Modifier.width(65.dp).height(30.dp)
                                        ) {
                                            Text(
                                                text = "Tomar",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = "Tomado",
                                            fontSize = 11.sp,
                                            color = PillBotMint,
                                            fontWeight = FontWeight.Bold
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
}