package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importaciones de tu proyecto (ajusta los paquetes según donde guardaste los archivos anteriores)
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.presentation.viewmodel.PerfilState
import com.jimenaoropeza.pillbot.presentation.viewmodel.PerfilViewModel


val AzulPillbot = Color(0xFF2298D4)
val VerdePillbot = Color(0xFF59CBA2)

@Composable
fun ConfiguracionPerfil(
    usuarioId: Int,
    onCerrarSesion: () -> Unit,
    viewModel: PerfilViewModel
) {
    // Escuchar el estado actual desde la API (Loading, Success o Error)
    val perfilState by viewModel.state.collectAsState()

    // Estados locales para los campos
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var contacto1 by remember { mutableStateOf("Juan Perez Cortes") }
    var telefono1 by remember { mutableStateOf("477-890-1234") }

    var contacto2 by remember { mutableStateOf("Maria Garcia Padilla") }
    var telefono2 by remember { mutableStateOf("477-567-8901") }

    var passwordActual by remember { mutableStateOf("") }
    var passwordNueva by remember { mutableStateOf("") }

    // 1. Disparar la consulta al backend en cuanto se cargue la pantalla o cambie usuarioId
    LaunchedEffect(usuarioId) {
        viewModel.obtenerPerfil(usuarioId)
    }

    // 2. Al recibir los datos del backend, rellenar los estados locales
    LaunchedEffect(perfilState) {
        if (perfilState is PerfilState.Success) {
            val persona = (perfilState as PerfilState.Success).usuario.persona
            if (persona != null) {
                val nombreCompleto = "${persona.nombre} ${persona.apellidoPaterno} ${persona.apellidoMaterno ?: ""}".trim()
                nombre = nombreCompleto
                correo = persona.correo
                telefono = persona.telefono ?: ""
            }
        }
    }

    // Pantalla principal de UI
    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = perfilState) {
            is PerfilState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VerdePillbot)
                }
            }

            is PerfilState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.mensaje, color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.obtenerPerfil(usuarioId) },
                            colors = ButtonDefaults.buttonColors(containerColor = VerdePillbot)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is PerfilState.Success -> {
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
                        text = "Configuración de perfil",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D2A44)
                    )

                    Text(
                        text = "Administra tu información",
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Tarjeta de foto de perfil superior
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF7FDFB)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(90.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_usuario),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )

                                IconButton(
                                    onClick = { /* Lógica para abrir galería o cámara */ },
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.BottomEnd)
                                        .background(color = Color.White, shape = CircleShape)
                                        .border(width = 2.dp, color = VerdePillbot, shape = CircleShape)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_camera),
                                        contentDescription = "Cambiar foto",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = correo,
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    TituloSeccion("Información personal")

                    CampoSimple(
                        titulo = "Nombre completo",
                        valor = nombre,
                        onValueChange = { nombre = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CampoSimple(
                            titulo = "Correo electrónico",
                            valor = correo,
                            onValueChange = { correo = it },
                            modifier = Modifier.weight(1f)
                        )

                        CampoSimple(
                            titulo = "Número telefónico",
                            valor = telefono,
                            onValueChange = { telefono = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    TituloSeccion("Contactos de emergencia")

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CampoSimple(
                            titulo = "Contacto 1",
                            valor = contacto1,
                            onValueChange = { contacto1 = it },
                            modifier = Modifier.weight(1f)
                        )

                        CampoSimple(
                            titulo = "Teléfono 1",
                            valor = telefono1,
                            onValueChange = { telefono1 = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CampoSimple(
                            titulo = "Contacto 2",
                            valor = contacto2,
                            onValueChange = { contacto2 = it },
                            modifier = Modifier.weight(1f)
                        )

                        CampoSimple(
                            titulo = "Teléfono 2",
                            valor = telefono2,
                            onValueChange = { telefono2 = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    TituloSeccion("Seguridad")

                    CampoPassword(
                        titulo = "Ingresa tu contraseña actual",
                        valor = passwordActual,
                        onValueChange = { passwordActual = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CampoPassword(
                        titulo = "Ingresa tu contraseña nueva",
                        valor = passwordNueva,
                        onValueChange = { passwordNueva = it }
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    Button(
                        onClick = {
                            /* Ejecutar update a la base de datos pasándole nombre, correo, telefono, etc. */
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdePillbot),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onCerrarSesion,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF356799)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
        color = AzulPillbot,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun CampoSimple(
    titulo: String,
    valor: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = titulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdePillbot,
                unfocusedBorderColor = Color(0xFFCCCCCC),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

@Composable
fun CampoPassword(
    titulo: String,
    valor: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = titulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdePillbot,
                unfocusedBorderColor = Color(0xFFCCCCCC),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}