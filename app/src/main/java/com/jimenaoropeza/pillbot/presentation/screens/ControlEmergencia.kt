package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlEmergencia(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val client = remember { OkHttpClient() }

    var estadoPastillero by remember { mutableStateOf("0") }
    var ultimaActualizacion by remember { mutableStateOf("") }

    fun actualizarEstado() {
        scope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder().url("http://pillbot.somee.com/salida.aspx").build()
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val body = response.body?.string()?.trim() ?: "0"
                        estadoPastillero = body
                        ultimaActualizacion = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    }
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            actualizarEstado()
            delay(5000)
        }
    }

    fun ejecutarAccion(comando: String) {
        scope.launch(Dispatchers.IO) {
            try {
                val body = FormBody.Builder().add("accion", comando).build()
                val request = Request.Builder().url("http://pillbot.somee.com/entrada.aspx").post(body).build()
                client.newCall(request).execute().use { }
                actualizarEstado()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFFF1F1F1), tonalElevation = 4.dp) {
                val navIcons = listOf(R.drawable.ic_inicio, R.drawable.ic_formulario, R.drawable.ic_notificacion, R.drawable.ic_inventario, R.drawable.ic_calendario, R.drawable.ic_emergencia, R.drawable.ic_perfil)
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
                                    Box(modifier = Modifier.size(16.dp).background(Color.Red, RoundedCornerShape(8.dp)).align(Alignment.TopEnd), contentAlignment = Alignment.Center) {
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
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF7F7F7)).padding(paddingValues).padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.drawable.logopastillero), contentDescription = null, modifier = Modifier.size(55.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "PILLBOT", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Control de emergencias", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Usa unica y exclusivamente para emergencias", color = Color.Gray)
            Spacer(modifier = Modifier.height(20.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FDFB)), elevation = CardDefaults.cardElevation(4.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (estadoPastillero == "1") Icons.Default.LockOpen else Icons.Default.Lock, null, tint = if (estadoPastillero == "1") Color.Red else Color(0xFF59CBA2), modifier = Modifier.size(42.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Estado del pastillero", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(if (estadoPastillero == "1") "⚠️ ¡Pastillero Abierto!" else "✓ Cerrado y seguro", color = if (estadoPastillero == "1") Color.Red else Color(0xFF59CBA2), fontSize = 12.sp)
                        Text("Última actualización: $ultimaActualizacion", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FDFB)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth() // Asegura que la columna ocupe el ancho del Card
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.LockOpen, null, tint = Color(0xFF2298D4), modifier = Modifier.size(42.dp))
                    Text("Abrir pastillero", color = Color(0xFF2298D4), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Abre todos los compartimientos para acceso inmediato.", fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { ejecutarAccion("EMERGENCIA_ON") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2298D4)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.LockOpen, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Abrir")
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FDFB)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Lock, null, tint = Color.Red, modifier = Modifier.size(42.dp))
                    Text("Cerrar pastillero", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Cierra todos los compartimientos para mayor seguridad.", fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { ejecutarAccion("EMERGENCIA_OFF") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Lock, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cerrar")
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FFFC)), border = BorderStroke(1.dp, Color(0xFF59CBA2))) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Información importante", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("• Usa estas opciones solo en caso de emergencia.\n• La apertura se registra en el historial.\n• Respeta la dosis y horario indicado.", fontSize = 10.sp, color = Color.DarkGray)
                }
            }
        }
    }
}