package com.jimenaoropeza.pillbot.pantallas

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.presentation.viewmodel.PerfilState
import com.jimenaoropeza.pillbot.presentation.viewmodel.PerfilViewModel
import java.util.Calendar

val AzulPillbot = Color(0xFF2298D4)
val VerdePillbot = Color(0xFF59CBA2)
val RojoCerrarSesion = Color(0xFFE57373)

@Composable
fun ConfiguracionPerfil(
    usuarioId: Int,
    onCerrarSesion: () -> Unit,
    viewModel: PerfilViewModel
) {
    val context = LocalContext.current
    val perfilState by viewModel.state.collectAsState()

    var esEditable by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Estados de Persona
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    // Estados de Cliente (Información Médica)
    var tipoSangre by remember { mutableStateOf("") }
    var alergias by remember { mutableStateOf("") }
    var contactoEmergencia by remember { mutableStateOf("") }
    var telefonoEmergencia by remember { mutableStateOf("") }

    // Estados de Seguridad (Contraseñas)
    var passwordNueva by remember { mutableStateOf("") }
    var passwordConfirmar by remember { mutableStateOf("") }

    // Validación de coincidencia de contraseña
    val passwordsNoCoinciden = passwordNueva.isNotBlank() &&
            passwordConfirmar.isNotBlank() &&
            passwordNueva != passwordConfirmar

    // Diálogo de Calendario
    if (showDatePicker && esEditable) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val mesFormateado = String.format("%02d", month + 1)
                val diaFormateado = String.format("%02d", dayOfMonth)
                fechaNacimiento = "$year-$mesFormateado-$diaFormateado"
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnDismissListener { showDatePicker = false }
            show()
        }
    }

    LaunchedEffect(usuarioId) {
        viewModel.obtenerPerfil(usuarioId)
    }

    LaunchedEffect(perfilState) {
        if (perfilState is PerfilState.Success) {
            val cliente = (perfilState as PerfilState.Success).cliente

            nombre = cliente.nombre ?: ""
            apellidoPaterno = cliente.apellidoPaterno ?: ""
            apellidoMaterno = cliente.apellidoMaterno ?: ""
            correo = cliente.correo ?: ""
            telefono = cliente.telefono ?: ""
            direccion = cliente.direccion ?: ""
            fechaNacimiento = cliente.fechaNacimiento?.split("T")?.get(0) ?: ""

            tipoSangre = cliente.tipoSangre ?: ""
            alergias = cliente.alergias ?: ""
            contactoEmergencia = cliente.contactoEmergencia ?: ""
            telefonoEmergencia = cliente.telefonoEmergencia ?: ""

            // Limpiar campos de contraseña al cargar/reorganizar
            passwordNueva = ""
            passwordConfirmar = ""
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = perfilState) {
            is PerfilState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = VerdePillbot)
                }
            }

            is PerfilState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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

                    Text(text = "Administra tu información", color = Color.Gray)

                    Spacer(modifier = Modifier.height(20.dp))

                    // Tarjeta de Avatar
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FDFB)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(80.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_usuario),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "$nombre $apellidoPaterno".trim(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                                Text(text = correo, color = Color.Gray, fontSize = 14.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    // --- SECCIÓN 1: INFORMACIÓN PERSONAL ---
                    TituloSeccion("Información personal")

                    CampoSimple(
                        titulo = "Nombre(s)",
                        valor = nombre,
                        onValueChange = { nombre = it },
                        enabled = esEditable
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CampoSimple(
                            titulo = "Apellido paterno",
                            valor = apellidoPaterno,
                            onValueChange = { apellidoPaterno = it },
                            enabled = esEditable,
                            modifier = Modifier.weight(1f)
                        )

                        CampoSimple(
                            titulo = "Apellido materno",
                            valor = apellidoMaterno,
                            onValueChange = { apellidoMaterno = it },
                            enabled = esEditable,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CampoSimple(
                            titulo = "Teléfono",
                            valor = telefono,
                            onValueChange = { telefono = it },
                            enabled = esEditable,
                            modifier = Modifier.weight(1f)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Fecha de nacimiento",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = fechaNacimiento,
                                onValueChange = {},
                                readOnly = true,
                                enabled = esEditable,
                                trailingIcon = {
                                    if (esEditable) {
                                        IconButton(onClick = { showDatePicker = true }) {
                                            Icon(Icons.Default.DateRange, contentDescription = "Cambiar fecha", tint = VerdePillbot)
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = esEditable) { showDatePicker = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VerdePillbot,
                                    unfocusedBorderColor = Color(0xFFCCCCCC),
                                    disabledBorderColor = Color(0xFFE0E0E0),
                                    disabledTextColor = Color.DarkGray,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    disabledContainerColor = Color(0xFFF5F5F5)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    // --- SECCIÓN 2: INFORMACIÓN MÉDICA ---
                    TituloSeccion("Información médica")

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CampoSimple(
                            titulo = "Tipo de sangre",
                            valor = tipoSangre,
                            onValueChange = { tipoSangre = it },
                            enabled = esEditable,
                            modifier = Modifier.weight(1f)
                        )

                        CampoSimple(
                            titulo = "Alergias",
                            valor = alergias,
                            onValueChange = { alergias = it },
                            enabled = esEditable,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CampoSimple(
                            titulo = "Contacto emergencia",
                            valor = contactoEmergencia,
                            onValueChange = { contactoEmergencia = it },
                            enabled = esEditable,
                            modifier = Modifier.weight(1f)
                        )

                        CampoSimple(
                            titulo = "Tel. Emergencia",
                            valor = telefonoEmergencia,
                            onValueChange = { telefonoEmergencia = it },
                            enabled = esEditable,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    // --- SECCIÓN 3: CONTACTO Y DOMICILIO ---
                    TituloSeccion("Contacto y Domicilio")

                    CampoSimple(
                        titulo = "Correo electrónico",
                        valor = correo,
                        onValueChange = { correo = it },
                        enabled = esEditable
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CampoSimple(
                        titulo = "Dirección de domicilio",
                        valor = direccion,
                        onValueChange = { direccion = it },
                        enabled = esEditable
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    // --- SECCIÓN 4: SEGURIDAD ---
                    TituloSeccion("Seguridad")

                    CampoPassword(
                        titulo = "Nueva contraseña (opcional)",
                        valor = passwordNueva,
                        onValueChange = { passwordNueva = it },
                        enabled = esEditable,
                        isError = passwordsNoCoinciden
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CampoPassword(
                        titulo = "Confirmar nueva contraseña",
                        valor = passwordConfirmar,
                        onValueChange = { passwordConfirmar = it },
                        enabled = esEditable,
                        isError = passwordsNoCoinciden
                    )

                    if (passwordsNoCoinciden) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Las contraseñas no coinciden",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // --- BOTONES DE ACCIÓN ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BotonAccionPerfil(
                            titulo = if (esEditable) "CANCELAR" else "EDITAR",
                            icono = Icons.Default.Edit,
                            colorFondo = if (esEditable) Color.LightGray else AzulPillbot,
                            colorTexto = if (esEditable) Color.DarkGray else Color.White,
                            onClick = {
                                if (esEditable) {
                                    passwordNueva = ""
                                    passwordConfirmar = ""
                                }
                                esEditable = !esEditable
                            },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        BotonAccionPerfil(
                            titulo = "GUARDAR",
                            icono = Icons.Default.Check,
                            colorFondo = if (esEditable) VerdePillbot else Color(0xFFA5D6A7),
                            colorTexto = Color.White,
                            onClick = {
                                if (!esEditable) {
                                    Toast.makeText(context, "Presiona EDITAR primero", Toast.LENGTH_SHORT).show()
                                    return@BotonAccionPerfil
                                }

                                if (passwordsNoCoinciden) {
                                    Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                                    return@BotonAccionPerfil
                                }

                                viewModel.actualizarPerfilDirecto(
                                    usuarioId = usuarioId,
                                    nombre = nombre,
                                    apellidoPaterno = apellidoPaterno,
                                    apellidoMaterno = apellidoMaterno,
                                    telefono = telefono,
                                    correo = correo,
                                    direccion = direccion,
                                    fechaNacimiento = fechaNacimiento,
                                    tipoSangre = tipoSangre,
                                    alergias = alergias,
                                    contactoEmergencia = contactoEmergencia,
                                    telefonoEmergencia = telefonoEmergencia,
                                    contrasenaNueva = passwordNueva.ifBlank { null }
                                ) { exito, mensaje ->
                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                                    if (exito) {
                                        esEditable = false
                                        viewModel.obtenerPerfil(usuarioId)
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        BotonAccionPerfil(
                            titulo = "SALIR",
                            icono = Icons.AutoMirrored.Filled.ExitToApp,
                            colorFondo = RojoCerrarSesion,
                            colorTexto = Color.White,
                            onClick = onCerrarSesion,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun BotonAccionPerfil(
    titulo: String,
    icono: ImageVector,
    colorFondo: Color,
    colorTexto: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(72.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = colorTexto,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = titulo,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto,
                textAlign = TextAlign.Center
            )
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
    enabled: Boolean = true,
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
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdePillbot,
                unfocusedBorderColor = Color(0xFFCCCCCC),
                disabledBorderColor = Color(0xFFE0E0E0),
                disabledTextColor = Color.DarkGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color(0xFFF5F5F5)
            )
        )
    }
}

@Composable
fun CampoPassword(
    titulo: String,
    valor: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false
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
            enabled = enabled,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdePillbot,
                unfocusedBorderColor = Color(0xFFCCCCCC),
                disabledBorderColor = Color(0xFFE0E0E0),
                disabledTextColor = Color.DarkGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color(0xFFF5F5F5)
            )
        )
    }
}