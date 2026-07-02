package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMedicamento(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit,
    onRecargarClick: () -> Unit,
    medicamento: Medicamento
) {
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF1F1F1), // Fondo gris claro unificado
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
                        onClick = {
                            onNavTabClick(routes[index])
                        },
                        icon = {
                            Box(modifier = Modifier.wrapContentSize()) {
                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp) // Tamaño unificado a 32.dp
                                )

                                // Globo de notificaciones pendientes sincronizado
                                if (index == 2 && totalNoLeidas > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(
                                                Color.Red,
                                                shape = RoundedCornerShape(8.dp)
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
                            // Círculo Mint con opacidad del 30% al estar seleccionado
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
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { onNavTabClick("inventario") },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar al inventario",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logopastillero),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "PILLBOT",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Detalle del medicamento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_pildora),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "${medicamento.nombre_medicamento} ${medicamento.gramaje_medicamento}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(text = "Cantidad actual: ${medicamento.inventario_actual} unidades")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Compartimiento: A1")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Próxima toma: 10:00 AM")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text =
                            if(medicamento.inventario_actual > 5)
                                "Stock suficiente"
                            else
                                "Stock bajo",
                        color =
                            if(medicamento.inventario_actual > 5)
                                Color(0xFF59CBA2)
                            else
                                Color(0xFFFF9800),
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    onRecargarClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF59CBA2)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Recargar medicamento")
            }
        }
    }
}

@Composable
fun AgregarMedicamento(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit
) {

    var nombre by remember { mutableStateOf("") }
    var dosis by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var minima by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF1F1F1), // Fondo gris claro unificado
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
                        onClick = {
                            onNavTabClick(routes[index])
                        },
                        icon = {
                            Box(modifier = Modifier.wrapContentSize()) {
                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp) // Tamaño unificado a 32.dp
                                )

                                // Globo de notificaciones pendientes sincronizado
                                if (index == 2 && totalNoLeidas > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(
                                                Color.Red,
                                                shape = RoundedCornerShape(8.dp)
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
                            // Círculo Mint con opacidad del 30% al estar seleccionado
                            indicatorColor = if (isSelected) Color(0xFF59CBA2).copy(alpha = 0.3f) else Color.Transparent
                        )
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(14.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { onNavTabClick("inventario") },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar al inventario",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logopastillero),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "PILLBOT",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Agregar medicamento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                "Registra un nuevo medicamento en el dispensador",
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_camara),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            "Escanear medicamento",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Toma una foto del medicamento",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        "Información del medicamento",
                        color = Color(0xFF59CBA2),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Nombre del medicamento")

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Dosis")

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Tipo de medicamento")

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Notas (opcional)")

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        "Información del inventario",
                        color = Color(0xFF59CBA2),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Cantidad inicial")

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Cantidad mínima para alerta")

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF59CBA2)
                )
            ) {
                Text("Registrar medicamento")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecargarMedicamento(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit
) {

    var cantidad by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var lote by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

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
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(14.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { onNavTabClick("inventario") },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar al inventario",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logopastillero),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "PILLBOT",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Recargar medicamento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_pildora),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Aspirina 500 mg",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Stock suficiente",
                            color = Color(0xFF59CBA2)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Información de la recarga",
                        color = Color(0xFF59CBA2),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Cantidad agregada")

                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { cantidad = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Fecha de caducidad")

                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Lote (opcional)")

                    OutlinedTextField(
                        value = lote,
                        onValueChange = { lote = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Notas (opcional)")

                    OutlinedTextField(
                        value = notas,
                        onValueChange = { notas = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF59CBA2)
                )
            ) {
                Text("Registrar recarga")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}