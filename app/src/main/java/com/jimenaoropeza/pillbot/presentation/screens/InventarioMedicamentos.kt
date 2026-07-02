package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.jimenaoropeza.pillbot.viewmodel.MedicamentoViewModel
import com.jimenaoropeza.pillbot.viewmodel.RecordatorioViewModel
import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.modelo.MedicamentoRequest
import com.jimenaoropeza.pillbot.modelo.RecordatorioRequest
import com.jimenaoropeza.pillbot.R

@Composable
fun InventarioMedicamentos(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit,
    onMedicamentoClick: (Medicamento) -> Unit,
    onAgregarMedicamentoClick: () -> Unit,
    viewModel: MedicamentoViewModel = viewModel()
) {
    // obtenemos los medicamentos desde el ViewModel
    val medicamentos =
        viewModel.medicamentos.value

    // consumir API al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarMedicamentos(
            usuarioId = 1
        )
    }

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

                val routes = listOf(
                    "inicio",
                    "formulario",
                    "notificaciones",
                    "inventario",
                    "calendario",
                    "controlEmergencia",
                    "perfil"
                )

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
        },
        // CAMBIO 1: Agregamos el parámetro flotante nativo del Scaffold aquí
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarMedicamentoClick,
                containerColor = Color(0xFF59CBA2),
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White
                )
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
                text = "Inventario de medicamentos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Administra los medicamentos almacenados en tu dispensador",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar medicamento...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            //las tarjetas se crean con la información de la API
            medicamentos.forEach { medicamento ->
                MedicamentoCard(
                    nombre = "${medicamento.nombre_medicamento} ${medicamento.gramaje_medicamento}",

                    cantidad = "${medicamento.inventario_actual} unidades",

                    proximaToma = "Pendiente",

                    stock =
                        if(medicamento.inventario_actual > 5)
                            "Stock suficiente"
                        else
                            "Stock bajo",

                    stockColor =
                        if(medicamento.inventario_actual > 5)
                            Color(0xFF59CBA2)
                        else
                            Color(0xFFFF9800),

                    onClick ={
                        onMedicamentoClick(medicamento)
                    }
                )
            }
            Spacer(modifier = Modifier.height(80.dp)) // Espacio extra para que el último elemento no quede tapado por el FAB
        }
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
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_pildora
                ),
                contentDescription = null,
                modifier = Modifier.size(55.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = nombre,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = cantidad,
                    fontSize = 12.sp
                )

                Text(
                    text = "Próxima toma $proximaToma",
                    fontSize = 12.sp
                )

                Text(
                    text = stock,
                    color = stockColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioManual(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit,
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

    val frecuencias = listOf(
        "Cada 4 horas",
        "Cada 6 horas",
        "Cada 8 horas",
        "Cada 12 horas",
        "Una vez al día"
    )

    var expanded by remember { mutableStateOf(false) }
    var frecuenciaSeleccionada by remember {
        mutableStateOf("Seleccionar frecuencia")
    }

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

                val routes = listOf(
                    "inicio",
                    "formulario",
                    "notificaciones",
                    "inventario",
                    "calendario",
                    "controlEmergencia",
                    "perfil"
                )

                navIcons.forEachIndexed { index, iconRes ->

                    val isSelected = currentScreen == routes[index]

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onNavTabClick(routes[index]) },
                        icon = {

                            Box(
                                modifier = Modifier.wrapContentSize()
                            ) {

                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )

                                if (index == 2 && totalNoLeidas > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(
                                                Color.Red,
                                                RoundedCornerShape(8.dp)
                                            )
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
                            indicatorColor =
                                if (isSelected)
                                    Color(0xFF59CBA2).copy(alpha = 0.3f)
                                else
                                    Color.Transparent
                        )
                    )
                }


    }
}

) { paddingValues ->Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logopastillero),
                    contentDescription = "Logo",
                    modifier = Modifier.size(55.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "PILLBOT",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 32.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Ingresar manualmente",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Completa la información del medicamento",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // INFORMACIÓN DEL MEDICAMENTO

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD9D9D9)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Información del medicamento",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = AzulPillbot,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        OutlinedTextField(
                            value = nombreMedicamento,
                            onValueChange = { nombreMedicamento = it },
                            label = { Text("Nombre del medicamento") },
                            placeholder = { Text("Paracetamol") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdePillbot,
                                unfocusedBorderColor = VerdePillbot
                            )
                        )

                        OutlinedTextField(
                            value = dosis,
                            onValueChange = { dosis = it },
                            label = { Text("Dosis") },
                            placeholder = { Text("500 mg") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdePillbot,
                                unfocusedBorderColor = VerdePillbot
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ADMINISTRACIÓN

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD9D9D9)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Información de administración",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = AzulPillbot,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = {
                                expanded = !expanded
                            },
                            modifier = Modifier.weight(1f)
                        ) {

                            OutlinedTextField(
                                value = frecuenciaSeleccionada,
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text("Frecuencia")
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = expanded
                                    )
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VerdePillbot,
                                    unfocusedBorderColor = VerdePillbot
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {
                                    expanded = false
                                }
                            ) {

                                frecuencias.forEach { opcion ->

                                    DropdownMenuItem(
                                        text = {
                                            Text(opcion)
                                        },
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
                            label = { Text("¿Cuántas unidades?") },
                            placeholder = { Text("1 tableta") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdePillbot,
                                unfocusedBorderColor = VerdePillbot
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // HORARIOS

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD9D9D9)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Horario y Fechas",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = AzulPillbot,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
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
                                modifier = Modifier.size(18.dp),
                                tint = Color.Unspecified
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdePillbot,
                            unfocusedBorderColor = VerdePillbot
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        OutlinedTextField(
                            value = fechaInicio,
                            onValueChange = { fechaInicio = it },
                            label = { Text("Fecha de inicio") },
                            placeholder = { Text("01/06/2026") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdePillbot,
                                unfocusedBorderColor = VerdePillbot
                            )
                        )

                        OutlinedTextField(
                            value = fechaFin,
                            onValueChange = { fechaFin = it },
                            label = { Text("Fecha de finalización") },
                            placeholder = { Text("10/06/2026") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdePillbot,
                                unfocusedBorderColor = VerdePillbot
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // INVENTARIO

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD9D9D9)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Inventario",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = AzulPillbot,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        OutlinedTextField(
                            value = cantidadDisponible,
                            onValueChange = { cantidadDisponible = it },
                            label = { Text("Cantidad disponible") },
                            placeholder = { Text("30 tabletas") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdePillbot,
                                unfocusedBorderColor = VerdePillbot
                            )
                        )

                        OutlinedTextField(
                            value = nivelMinimo,
                            onValueChange = { nivelMinimo = it },
                            label = { Text("Nivel mínimo alerta") },
                            placeholder = { Text("5 tabletas") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdePillbot,
                                unfocusedBorderColor = VerdePillbot
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // OBSERVACIONES

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD9D9D9)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Observaciones",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = AzulPillbot,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = observaciones,
                        onValueChange = { observaciones = it },
                        label = {
                            Text("Indicaciones adicionales (opcional)")
                        },
                        placeholder = {
                            Text("Tomar después de los alimentos")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdePillbot,
                            unfocusedBorderColor = VerdePillbot
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                            onSuccess = {
                                onNavTabClick("inventario")
                            }
                        )
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdePillbot
            )
        ) {
            Text(
                text = "Guardar Medicamento",
                color = Color.White
            )
        }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    onNavTabClick("formulario")
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePillbot
                )
            ) {
                Text(
                    text = "Volver",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

fun calcularDiasTratamiento(
    fechaInicio: String,
    fechaFin: String
): Int {
    return try {
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val inicio = LocalDate.parse(
            fechaInicio,
            formato
        )
        val fin = LocalDate.parse(
            fechaFin,
            formato
        )
        val dias = ChronoUnit.DAYS.between(
            inicio,
            fin
        ).toInt() + 1
        if (dias > 0) dias else 1
    } catch (e: Exception) {
        e.printStackTrace()
        1
    }
}