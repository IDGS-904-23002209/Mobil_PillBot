package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.modelo.MedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.RecordatorioRequest
import com.jimenaoropeza.pillbot.viewmodel.MedicamentoViewModel
import com.jimenaoropeza.pillbot.viewmodel.RecordatorioViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// =========================================================================
// 1. INVENTARIO DE MEDICAMENTOS (LISTA PRINCIPAL)
// =========================================================================
@Composable
fun InventarioMedicamentosScreen(
    medicamentos: List<Medicamento>,
    onMedicamentoClick: (Medicamento) -> Unit
) {
    var query by remember { mutableStateOf("") }

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
            text = "Inventario de medicamentos",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D2A44)
        )

        Text(
            text = "Administra los medicamentos almacenados en tu dispensador",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Buscar medicamento...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF59CBA2),
                unfocusedBorderColor = Color(0xFFDCDCDC)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Filtrado en tiempo real desde la barra de búsqueda
        medicamentos.filter { it.nombre_medicamento.contains(query, ignoreCase = true) }.forEach { medicamento ->
            MedicamentoCard(
                nombre = "${medicamento.nombre_medicamento} ${medicamento.gramaje_medicamento}",
                cantidad = "${medicamento.inventario_actual} unidades",
                proximaToma = "Pendiente",
                stock = if (medicamento.inventario_actual > 5) "Stock suficiente" else "Stock bajo",
                stockColor = if (medicamento.inventario_actual > 5) Color(0xFF59CBA2) else Color(0xFFFF9800),
                onClick = { onMedicamentoClick(medicamento) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun MedicamentoCard(
    nombre: String,
    cantidad: String,
    proximaToma: String,
    stock: String,
    stockColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FDFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pildora),
                contentDescription = null,
                modifier = Modifier.size(55.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = nombre, fontWeight = FontWeight.Bold, color = Color(0xFF1D2A44))
                Text(text = cantidad, fontSize = 12.sp, color = Color.Gray)
                Text(text = "Próxima toma: $proximaToma", fontSize = 12.sp, color = Color.Gray)
                Text(text = stock, color = stockColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}


// =========================================================================
// 2. FORMULARIO MANUAL DE REGISTRO
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioManual(
    currentScreen: String,
    onNavTabClick: (String) -> Unit, // Acoplado perfectamente a la firma de PillBotNavigation
    viewModel: MedicamentoViewModel,
    recordatorioViewModel: RecordatorioViewModel
) {
    val AzulPillbot = Color(0xFF2298D4)
    val VerdePillbot = Color(0xFF59CBA2)

    var nombreMedicamento by remember { mutableStateOf("") }
    var dosis by remember { mutableStateOf("") }
    var unidades by remember { mutableStateOf("") }
    var horaPrimeraToma by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var cantidadDisponible by remember { mutableStateOf("") }
    var nivelMinimo by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }

    val frecuencias = listOf("Cada 4 horas", "Cada 6 horas", "Cada 8 horas", "Cada 12 horas", "Una vez al día")
    var expanded by remember { mutableStateOf(false) }
    var frecuenciaSeleccionada by remember { mutableStateOf("Seleccionar frecuencia") }

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
            text = "Ingresar manualmente",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D2A44)
        )
        Text(
            text = "Completa la información del medicamento",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // TARJETA: INFORMACIÓN BÁSICA
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Información del medicamento",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = AzulPillbot,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = nombreMedicamento,
                        onValueChange = { nombreMedicamento = it },
                        label = { Text("Nombre") },
                        placeholder = { Text("Paracetamol") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePillbot, unfocusedBorderColor = VerdePillbot)
                    )
                    OutlinedTextField(
                        value = dosis,
                        onValueChange = { dosis = it },
                        label = { Text("Dosis") },
                        placeholder = { Text("500 mg") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePillbot, unfocusedBorderColor = VerdePillbot)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TARJETA: ADMINISTRACIÓN
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Información de administración",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = AzulPillbot,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = frecuenciaSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Frecuencia") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePillbot, unfocusedBorderColor = VerdePillbot)
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            frecuencias.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion) },
                                    onClick = {
                                        frecuenciaSeleccionada = opcion
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = unidades,
                        onValueChange = { unidades = it },
                        label = { Text("Unidades") },
                        placeholder = { Text("1 tableta") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePillbot, unfocusedBorderColor = VerdePillbot)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TARJETA: HORARIOS Y VIGENCIA
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Horario y Fechas",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = AzulPillbot,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = horaPrimeraToma,
                    onValueChange = { horaPrimeraToma = it },
                    label = { Text("Horario de primera toma") },
                    placeholder = { Text("08:00 AM") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePillbot, unfocusedBorderColor = VerdePillbot)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = fechaInicio,
                        onValueChange = { fechaInicio = it },
                        label = { Text("Inicio") },
                        placeholder = { Text("01/06/2026") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePillbot, unfocusedBorderColor = VerdePillbot)
                    )
                    OutlinedTextField(
                        value = fechaFin,
                        onValueChange = { fechaFin = it },
                        label = { Text("Fin") },
                        placeholder = { Text("10/06/2026") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePillbot, unfocusedBorderColor = VerdePillbot)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TARJETA: CONTROL DE STOCK
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Inventario",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = AzulPillbot,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = cantidadDisponible,
                        onValueChange = { cantidadDisponible = it },
                        label = { Text("Disponible") },
                        placeholder = { Text("30") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePillbot, unfocusedBorderColor = VerdePillbot)
                    )
                    OutlinedTextField(
                        value = nivelMinimo,
                        onValueChange = { nivelMinimo = it },
                        label = { Text("Mínimo alerta") },
                        placeholder = { Text("5") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePillbot, unfocusedBorderColor = VerdePillbot)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TARJETA: OBSERVACIONES EXTRA
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Observaciones",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = AzulPillbot,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { observaciones = it },
                    label = { Text("Indicaciones adicionales (opcional)") },
                    placeholder = { Text("Tomar después de los alimentos") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePillbot, unfocusedBorderColor = VerdePillbot)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN: ACCIÓN DE GUARDADO REMOTO
        Button(
            onClick = {
                val medicamento = MedicamentoRequest(
                    id_usuario = 1,
                    nombre_medicamento = nombreMedicamento,
                    gramaje_medicamento = dosis,
                    inventario_inicial = cantidadDisponible.toIntOrNull() ?: 0,
                    descripcion = observaciones
                )

                viewModel.guardarMedicamento(
                    medicamento = medicamento,
                    onSuccess = { medicamentoCreado ->
                        val frecuenciaHoras = when (frecuenciaSeleccionada) {
                            "Cada 4 horas" -> 4
                            "Cada 6 horas" -> 6
                            "Cada 8 horas" -> 8
                            "Cada 12 horas" -> 12
                            "Una vez al día" -> 24
                            else -> 24
                        }

                        // Corregido a 'dias_tratamiento' nativo en español para el modelo de datos
                        val recordatorio = RecordatorioRequest(
                            id_usuario = 1,
                            id_medicamento = medicamentoCreado.id_medicamento,
                            hora_toma = horaPrimeraToma,
                            frecuencia_horas = frecuenciaHoras,
                            dosis = unidades,
                            dias_tratamiento = calcularDiasTratamiento(fechaInicio, fechaFin)
                        )

                        recordatorioViewModel.guardarRecordatorio(
                            recordatorio = recordatorio,
                            onSuccess = { onNavTabClick("inventario") }
                        )
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(0.7f).height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdePillbot)
        ) {
            Text(text = "Guardar Medicamento", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { onNavTabClick("inventario") },
            modifier = Modifier.fillMaxWidth(0.7f).height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdePillbot)
        ) {
            Text(text = "Volver", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

fun calcularDiasTratamiento(fechaInicio: String, fechaFin: String): Int {
    return try {
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val inicio = LocalDate.parse(fechaInicio, formato)
        val fin = LocalDate.parse(fechaFin, formato)
        val dias = ChronoUnit.DAYS.between(inicio, fin).toInt() + 1
        if (dias > 0) dias else 1
    } catch (e: Exception) {
        e.printStackTrace()
        1
    }
}