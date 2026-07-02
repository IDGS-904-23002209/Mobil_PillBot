package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
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
import com.jimenaoropeza.pillbot.modelo.Medicamento // Asegúrate de que apunte a tu modelo real

@Composable
fun DetalleMedicamento(
    medicamento: Medicamento,
    onVolver: () -> Unit,
    onRecargarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

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
                    contentDescription = "Regresar al inventario",
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xFF1D2A44)
                )
            }

            Text(
                text = "Detalle del medicamento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D2A44)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_pildora),
            contentDescription = null,
            modifier = Modifier.size(90.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "${medicamento.nombre_medicamento} ${medicamento.gramaje_medicamento}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D2A44)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FDFB)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Cantidad actual: ${medicamento.inventario_actual} unidades", color = Color.DarkGray, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Compartimiento: A1", color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Próxima toma: 10:00 AM", color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (medicamento.inventario_actual > 5) "Stock suficiente" else "Stock bajo",
                    color = if (medicamento.inventario_actual > 5) Color(0xFF59CBA2) else Color(0xFFFF9800),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { onRecargarClick() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF59CBA2)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.8f).height(48.dp)
        ) {
            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Recargar medicamento", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AgregarMedicamento(
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var dosis by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }
    var cantidadInicial by remember { mutableStateOf("") }
    var cantidadMinima by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(horizontal = 14.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(12.dp))

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
                    contentDescription = "Regresar al inventario",
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xFF1D2A44)
                )
            }

            Text(
                "Agregar medicamento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D2A44)
            )
        }

        Text(
            "Registra un nuevo medicamento en el dispensador",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card de Escaneo
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_camara),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Escanear medicamento", fontWeight = FontWeight.Bold, color = Color(0xFF1D2A44))
                    Text("Toma una foto del medicamento", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card de Información General
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información del medicamento", color = Color(0xFF59CBA2), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                Text("Nombre del medicamento", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF59CBA2), unfocusedBorderColor = Color(0xFFDCDCDC))
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Dosis", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                OutlinedTextField(
                    value = dosis, // Corrige bug de variables cruzadas
                    onValueChange = { dosis = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF59CBA2), unfocusedBorderColor = Color(0xFFDCDCDC))
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Tipo de medicamento", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                OutlinedTextField(
                    value = tipo, // Corrige bug de variables cruzadas
                    onValueChange = { tipo = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF59CBA2), unfocusedBorderColor = Color(0xFFDCDCDC))
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Notas (opcional)", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                OutlinedTextField(
                    value = notas, // Corrige bug de variables cruzadas
                    onValueChange = { notas = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF59CBA2), unfocusedBorderColor = Color(0xFFDCDCDC))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card de Información del Inventario
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información del inventario", color = Color(0xFF59CBA2), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                Text("Cantidad inicial", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                OutlinedTextField(
                    value = cantidadInicial, // Corrige bug de variables cruzadas
                    onValueChange = { cantidadInicial = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF59CBA2), unfocusedBorderColor = Color(0xFFDCDCDC))
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Cantidad mínima para alerta", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                OutlinedTextField(
                    value = cantidadMinima, // Corrige bug de variables cruzadas
                    onValueChange = { cantidadMinima = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF59CBA2), unfocusedBorderColor = Color(0xFFDCDCDC))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Acción de inserción */ },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF59CBA2)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Registrar medicamento", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun RecargarMedicamento(
    onVolver: () -> Unit
) {
    var cantidad by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var lote by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(horizontal = 14.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(12.dp))

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
                    contentDescription = "Regresar al inventario",
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xFF1D2A44)
                )
            }

            Text(
                text = "Recargar medicamento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D2A44)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Medicamento actual a recargar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pildora),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Aspirina 500 mg", fontWeight = FontWeight.Bold, color = Color(0xFF1D2A44))
                    Text(text = "Stock suficiente", color = Color(0xFF59CBA2), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Formulario de recarga
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Información de la recarga",
                    color = Color(0xFF59CBA2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Cantidad agregada", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF59CBA2), unfocusedBorderColor = Color(0xFFDCDCDC))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Fecha de caducidad", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF59CBA2), unfocusedBorderColor = Color(0xFFDCDCDC))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Lote (opcional)", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                OutlinedTextField(
                    value = lote,
                    onValueChange = { lote = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF59CBA2), unfocusedBorderColor = Color(0xFFDCDCDC))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Notas (opcional)", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                OutlinedTextField(
                    value = notas,
                    onValueChange = { notas = it },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF59CBA2), unfocusedBorderColor = Color(0xFFDCDCDC))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Acción para actualizar inventario */ },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF59CBA2)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Registrar recarga", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}