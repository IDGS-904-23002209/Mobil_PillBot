package com.jimenaoropeza.pillbot.pantallas

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimenaoropeza.pillbot.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jimenaoropeza.pillbot.modelo.TomaHoy
import com.jimenaoropeza.pillbot.viewmodel.TomaHoyViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inicio(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit,
    viewModel: TomaHoyViewModel = viewModel(),
    usuarioId: Int, // Recibe el ID de usuario dinámico desde MainActivity
    nombreUsuario: String
) {
    val tomasHoy = viewModel.tomasHoy.value

    // Lanza la petición usando el ID del usuario autenticado dinámicamente
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
                val routes = listOf("inicio", "formulario", "notificaciones", "inventario", "calendario", "controlEmergencia", "perfil")
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
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
                text = "Bienvenido, $nombreUsuario",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (tomasPendientes > 0) "Tienes medicamentos pendientes por tomar" else "No tienes medicamentos pendientes",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Hoy, $fechaFormateada",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "$tomasPendientes medicamento(s) pendiente(s)",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start)
            )

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
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (tomasHoy.isEmpty()) {
                Text(
                    text = "No hay tomas registradas para hoy.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                tomasHoy.forEach { toma ->
                    MedicamentItem(toma = toma)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun MedicamentItem(toma: TomaHoy) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC5D6E6))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF001A66))
                    .padding(vertical = 4.dp, horizontal = 16.dp)
            ) {
                // Solución 1: Si la hora es nula, muestra un guion de respaldo seguro
                Text(
                    text = toma.hora_toma ?: "--:--",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pildora),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    // Solución 2: Si el nombre del medicamento es nulo, evita el crash mostrando un texto genérico
                    Text(
                        text = toma.nombre_medicamento ?: "Medicamento sin nombre",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    // Solución 3: Hacemos lo mismo con la dosis por seguridad
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
        }
    }
}