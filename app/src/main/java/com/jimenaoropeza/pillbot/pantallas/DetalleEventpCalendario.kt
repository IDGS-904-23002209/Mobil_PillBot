package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.ui.theme.*

@Composable
fun PillBotEventDetailScreen(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit
) {
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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Encabezado Unificado PillBot
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onNavTabClick("calendario") },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar al inventario",
                            modifier = Modifier.size(28.dp)
                        )
                    }

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
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Eventos por dia",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PillBotLightBlue, RoundedCornerShape(8.dp))
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Lunes, 08 de Junio del 2026 ", fontWeight = FontWeight.Bold, color = PillBotNavy)
                }

                Spacer(modifier = Modifier.height(8.dp))

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

                // Cuerpo de la Agenda Temporal
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    Column(
                        modifier = Modifier
                            .width(65.dp)
                            .fillMaxHeight()
                            .background(PillBotMint.copy(alpha = 0.25f), RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                    ) {
                        val hours = listOf("9 AM", "11 AM", "12 PM", "1 PM")
                        hours.forEach { hour ->
                            Box(modifier = Modifier.fillMaxWidth().height(90.dp).padding(start = 8.dp), contentAlignment = Alignment.CenterStart) {
                                Text(text = hour, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PillBotNavy)
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())) {
                        DetailTableRow {
                            DetailEventCard(title = "Dosis vencida/\npendiente: Chem-\nCall 1:50ml\n(Líquido\npersonalizado)|\n12:00 PM.", color = PillBotMint, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(4.dp))
                            Spacer(modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(4.dp))
                            DetailEventCard(title = "Pepto bismol", color = PillBotBlueEvent, textColor = Color.White, modifier = Modifier.weight(1f))
                        }

                        DetailTableRow {
                            DetailEventCard(title = "Dosis vencida/\npendiente:\nAmoxicilina\n1:50ml (Líquido\npersonalizado)|\n12:00 PM.", color = PillBotMint, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(4.dp))
                            DetailEventCard(title = "Vitamina A\nDosis: 1/1\n\nVitamina B\nDosis: 2/10\n\nVitamina E\nDosis: 1/23", color = PillBotYellow, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(4.dp))
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        DetailTableRow {
                            Spacer(modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(4.dp))
                            Spacer(modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(4.dp))
                            DetailEventCard(title = "Pomada de la\ncampana", color = PillBotBlueEvent, textColor = Color.White, modifier = Modifier.weight(1f))
                        }

                        DetailTableRow {
                            Spacer(modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(4.dp))
                            DetailEventCard(title = "Vitamina D\nDosis: 1/1\n\nVitamina C\nDosis: 2/10", color = PillBotYellow, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(4.dp))
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }

            // Alerta Flotante
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .align(Alignment.Center)
                    .background(PillBotMint.copy(alpha = 0.95f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🧴", fontSize = 32.sp, modifier = Modifier.padding(end = 12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Dosis vencida/pendiente: Paracetamol 1:50ml | 12:00 PM.", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PillBotNavy)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(14.dp).background(Color.White.copy(alpha = 0.4f), CircleShape)) {
                            Box(modifier = Modifier.fillMaxWidth(0.6f).fillMaxHeight().background(Color.White, CircleShape), contentAlignment = Alignment.CenterEnd) {
                                Text("60%  ", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PillBotNavy)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailCategoryChip(text: String, color: Color, modifier: Modifier = Modifier, textColor: Color = PillBotNavy) {
    Card(shape = RoundedCornerShape(4.dp), colors = CardDefaults.cardColors(containerColor = color), modifier = modifier.padding(horizontal = 2.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(35.dp), contentAlignment = Alignment.Center) {
            Text(text = text, fontSize = 10.sp, lineHeight = 11.sp, fontWeight = FontWeight.Bold, color = textColor, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun DetailTableRow(content: @Composable RowScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().height(90.dp).padding(start = 6.dp, end = 4.dp, top = 4.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
            content()
        }
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Composable
fun DetailEventCard(title: String, color: Color, modifier: Modifier = Modifier, textColor: Color = PillBotNavy) {
    Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = color), modifier = modifier.fillMaxHeight().padding(vertical = 2.dp), elevation = CardDefaults.cardElevation(0.dp)) {
        Text(text = title, fontSize = 9.sp, lineHeight = 11.sp, fontWeight = FontWeight.SemiBold, color = textColor, modifier = Modifier.padding(6.dp))
    }
}