package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.NotificacionItem
import com.jimenaoropeza.pillbot.viewmodel.TomaHoyViewModel
import com.jimenaoropeza.pillbot.viewmodel.HistorialViewModel
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.modelo.HistorialRequest
import  java.time.LocalTime
import  java.time.LocalDate
import  java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notificaciones(
    currentScreen: String,
    listaNotificaciones: MutableList<NotificacionItem>,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit,
    viewModel: TomaHoyViewModel = viewModel(),
    historialViewModel: HistorialViewModel = viewModel()
) {
    var selectedSubTab by remember { mutableStateOf(0) }
    val tomasHoy = viewModel.tomasHoy.value
    LaunchedEffect(Unit) {viewModel.cargarTomasHoy(usuarioId = 1)}
    val tomasFiltradas = if (selectedSubTab == 0) {tomasHoy} else {tomasHoy.filter { !it.tomado }}

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF1F1F1),
                tonalElevation = 4.dp
            ) {
                val navIcons = listOf(
                    R.drawable.ic_inicio,
                    R.drawable.ic_formulario,
                    R.drawable.ic_notificacion,
                    R.drawable.ic_inventario,
                    R.drawable.ic_calendario,
                    R.drawable.ic_emergencia,
                    R.drawable.ic_perfil
                )
                val routes = listOf("inicio", "formulario", "notificaciones", "inventario", "calendario", "controlEmergencia","perfil")
                navIcons.forEachIndexed { index, iconRes ->
                    val isSelected = currentScreen == routes[index]

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onNavTabClick(routes[index]) },
                        icon = {
                            Box(modifier = Modifier.wrapContentSize()) {
                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )

                                if (index == 2 && totalNoLeidas > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(Color.Red, shape = RoundedCornerShape(8.dp))
                                            .align(Alignment.TopEnd),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = totalNoLeidas.toString(),
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = if (isSelected) Color(0xFF59CBA2).copy(alpha = 0.3f) else Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

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
                text = "Tus notificaiones",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Administra tu dispensador",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Selector de Filtros (Todas | No leídas)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(20.dp))
                    .padding(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { selectedSubTab = 0 },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedSubTab == 0) Color(0xFF59CBA2) else Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Todas",
                        color = if (selectedSubTab == 0) Color.White else Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Button(
                    onClick = { selectedSubTab = 1 },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedSubTab == 1) Color(0xFF59CBA2) else Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "No leídas",
                        color = if (selectedSubTab == 1) Color.White else Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (tomasFiltradas.isEmpty()) {
                    Text(
                        text = "No hay notificaciones aquí.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    tomasFiltradas.forEach { toma ->
                        NotificationCard(
                            toma = toma,
                            historialViewModel = historialViewModel,
                            onCardClick = {onNavTabClick("calendario")},
                            onTomaRegistrada = {viewModel.cargarTomasHoy(usuarioId = 1)}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(toma: TomaHoy, historialViewModel: HistorialViewModel, onCardClick: () -> Unit, onTomaRegistrada: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (toma.tomado)
                    Color.White
                else
                    Color(0xFFF7FDFB)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.ic_notificacion), contentDescription = null, modifier = Modifier.size(42.dp))

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    // CORRECCIÓN: Operador elvis para el nombre del medicamento
                    Text(text = "Toma de ${toma.nombre_medicamento ?: "Medicamento sin nombre"}", fontSize = 13.sp, fontWeight = if (toma.tomado) FontWeight.Medium else FontWeight.Bold, color = Color.Black, modifier = Modifier.weight(1f), lineHeight = 16.sp)

                    // CORRECCIÓN: Operador elvis para la hora
                    Text(text = toma.hora_toma ?: "--:--", fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.End)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // CORRECCIÓN: Operador elvis para la dosis
                Text(
                    text = "Dosis: ${toma.dosis ?: "No especificada"} • ${if (toma.tomado) "Tomado" else "Pendiente"}",
                    fontSize = 12.sp,
                    color = if (toma.tomado) Color.Gray else Color.DarkGray, lineHeight = 15.sp
                )

                if (!toma.tomado) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            // CORRECCIÓN: Se cambió id_recordatorio por id_toma que es el nombre real en tu Data Class
                            val historial = HistorialRequest(
                                id_recordatorio = toma.id_toma,
                                fecha_registro = LocalDate.now().toString(),
                                hora_registro = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                                estatus = "tomado"
                            )
                            historialViewModel.registrarToma(historial = historial, onSuccess = {onTomaRegistrada()})
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF59CBA2))
                    ) {
                        Text(text = "Marcar como tomado", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}