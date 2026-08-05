package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.viewmodel.AuthViewModel
import com.jimenaoropeza.pillbot.ui.theme.BlueSky
import com.jimenaoropeza.pillbot.ui.theme.GrayLight

import android.app.DatePickerDialog
import android.util.Patterns
import androidx.compose.material.icons.filled.AddIcCall
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.*
import java.util.Calendar
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onForgotPasswordClick: () -> Unit,
    onLoginSuccess: (Int, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.imagenfondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.20f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                // CORREGIDO: Se añade el modificador verticalScroll para soportar el desplazamiento
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 32.dp, top = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logopastillero),
                    contentDescription = "Logotipo de Pillbot",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "PILLBOT",
                        fontSize = 45.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.Black
                    )
                    Text(
                        text = "Tu dispensador de pastillas\ninteligente",
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                text = "¡Bienvenido Usuario!",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nombre de usuario o correo",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = usernameOrEmail,
                    onValueChange = { usernameOrEmail = it },
                    placeholder = { Text("Ejemplo@gmail.com", color = GrayLight) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = GrayLight) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF59CBA2),
                        unfocusedBorderColor = GrayLight
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Contraseña",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("********", color = GrayLight) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = GrayLight
                        )
                    },
                    trailingIcon = {
                        // CORREGIDO: Cambio de estado correcto y uso de iconos nativos de material
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = GrayLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF59CBA2),
                        unfocusedBorderColor = Color(0xFF59CBA2)
                    )
                )
            }

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = BlueSky,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp)
                    .clickable { onForgotPasswordClick() }
            )

            if (viewModel.isLoading) {
                CircularProgressIndicator(color = Color(0xFF59CBA2), modifier = Modifier.padding(16.dp))
            }

            viewModel.errorMessage?.let { error ->
                Text(text = error, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
            }

            Button(
                onClick = {
                    if (usernameOrEmail.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.iniciarSesion(usernameOrEmail, password) {
                            onLoginSuccess(viewModel.usuarioId, viewModel.usuarioNombre)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF59CBA2)
                )
            ) {
                Text(
                    text = "INICIAR SESIÓN",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(text = "¿No tienes una cuenta? ", color = Color.Black, fontSize = 14.sp)
                Text(
                    text = "Regístrate",
                    color = BlueSky,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registrarse(
    viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackToLoginClick: () -> Unit,
    onRegisterSuccessClick: () -> Unit
) {
    val context = LocalContext.current

    var passwordVisible by remember { mutableStateOf(false) }
    var errorValidacion by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    // --- DIÁLOGO DEL CALENDARIO ---
    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val mesFormateado = String.format("%02d", month + 1)
                val diaFormateado = String.format("%02d", dayOfMonth)
                viewModel.fechaNacimiento = "$year-$mesFormateado-$diaFormateado"
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.imagenfondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.20f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Cabecera Pillbot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logopastillero),
                    contentDescription = "Logotipo de Pillbot",
                    modifier = Modifier.size(70.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "PILLBOT",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Tu dispensador de pastillas\ninteligente",
                        fontSize = 12.sp,
                        lineHeight = 15.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                text = "¡Crea tu cuenta!",
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Nombre
            CampoTexto(
                label = "Nombre *",
                value = viewModel.nombre,
                onValueChange = { viewModel.nombre = it },
                placeholder = "Juan",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Apellido Paterno
            CampoTexto(
                label = "Apellido Paterno *",
                value = viewModel.apellidoPaterno,
                onValueChange = { viewModel.apellidoPaterno = it },
                placeholder = "García",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Apellido Materno
            CampoTexto(
                label = "Apellido Materno",
                value = viewModel.apellidoMaterno,
                onValueChange = { viewModel.apellidoMaterno = it },
                placeholder = "Pérez",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Fecha de Nacimiento con Calendario
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Fecha de Nacimiento *",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = viewModel.fechaNacimiento,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("AAAA-MM-DD", color = GrayLight) },
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = GrayLight) },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Abrir Calendario", tint = Color(0xFF59CBA2))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF59CBA2),
                        unfocusedBorderColor = Color(0xFF59CBA2)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Dirección
            CampoTexto(
                label = "Dirección Completa *",
                value = viewModel.direccion,
                onValueChange = { viewModel.direccion = it },
                placeholder = "Av. Siempre Viva 123, León, Gto.",
                icon = Icons.Default.Home
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 6. Teléfono
            CampoTexto(
                label = "Teléfono",
                value = viewModel.telefono,
                onValueChange = { viewModel.telefono = it },
                placeholder = "4771234567",
                icon = Icons.Default.AddIcCall,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 7. Correo Electrónico
            CampoTexto(
                label = "Correo Electrónico *",
                value = viewModel.correo,
                onValueChange = { viewModel.correo = it },
                placeholder = "ejemplo@gmail.com",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- SECCIÓN: INFORMACIÓN MÉDICA (CLIENTE) ---
            Text(
                text = "Información Médica (Opcional)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF59CBA2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Tipo de Sangre
            CampoTexto(
                label = "Tipo de Sangre",
                value = viewModel.tipoSangre,
                onValueChange = { viewModel.tipoSangre = it },
                placeholder = "O+, A-, B+, etc.",
                icon = Icons.Default.Favorite
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Alergias
            CampoTexto(
                label = "Alergias",
                value = viewModel.alergias,
                onValueChange = { viewModel.alergias = it },
                placeholder = "Penicilina, Polvo, etc.",
                icon = Icons.Default.Warning
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contacto de Emergencia
            CampoTexto(
                label = "Contacto de Emergencia",
                value = viewModel.contactoEmergencia,
                onValueChange = { viewModel.contactoEmergencia = it },
                placeholder = "Nombre del familiar o contacto",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Teléfono de Emergencia
            CampoTexto(
                label = "Teléfono de Emergencia",
                value = viewModel.telefonoEmergencia,
                onValueChange = { viewModel.telefonoEmergencia = it },
                placeholder = "4771234567",
                icon = Icons.Default.Call,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 8. Tipo de Cuenta
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Tipo de Cuenta",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = viewModel.idRol == 3,
                            onClick = { viewModel.idRol = 3 },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF59CBA2))
                        )
                        Text("Cliente", fontSize = 14.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = viewModel.idRol == 4,
                            onClick = { viewModel.idRol = 4 },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF59CBA2))
                        )
                        Text("Cuidador", fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 9. Contraseña
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Contraseña *",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = viewModel.contrasena,
                    onValueChange = { viewModel.contrasena = it },
                    placeholder = { Text("********", color = GrayLight) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GrayLight) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(id = if (passwordVisible) android.R.drawable.ic_menu_view else R.drawable.ic_visibility_off),
                                contentDescription = null,
                                tint = GrayLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF59CBA2),
                        unfocusedBorderColor = Color(0xFF59CBA2)
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- ERRORES ---
            errorValidacion?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            viewModel.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (viewModel.isLoading) {
                CircularProgressIndicator(color = Color(0xFF59CBA2), modifier = Modifier.padding(16.dp))
            }

            // --- BOTÓN REGISTRARSE ---
            Button(
                onClick = {
                    errorValidacion = null

                    when {
                        viewModel.nombre.trim().isEmpty() -> errorValidacion = "Por favor ingresa tu nombre."
                        viewModel.apellidoPaterno.trim().isEmpty() -> errorValidacion = "Por favor ingresa tu apellido paterno."
                        viewModel.fechaNacimiento.trim().isEmpty() -> errorValidacion = "Por favor selecciona tu fecha de nacimiento."
                        viewModel.direccion.trim().isEmpty() -> errorValidacion = "Por favor ingresa tu dirección."
                        viewModel.correo.trim().isEmpty() -> errorValidacion = "Por favor ingresa tu correo."
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(viewModel.correo.trim()).matches() -> errorValidacion = "El correo no tiene un formato válido."
                        viewModel.contrasena.trim().isEmpty() -> errorValidacion = "Por favor ingresa una contraseña."
                        viewModel.contrasena.length < 6 -> errorValidacion = "La contraseña debe tener al menos 6 caracteres."
                        else -> {
                            viewModel.registrarUsuario {
                                // MUESTRA EL MENSAJE DE ÉXITO
                                Toast.makeText(
                                    context,
                                    "¡Registro realizado con éxito! Ya puedes iniciar sesión.",
                                    Toast.LENGTH_LONG
                                ).show()

                                // NAVEGA AL LOGIN
                                onRegisterSuccessClick()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF59CBA2))
            ) {
                Text(
                    text = "REGISTRARSE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Volver al Login
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "¿Ya tienes una cuenta? ", color = Color.Black, fontSize = 14.sp)
                TextButton(
                    onClick = { onBackToLoginClick() },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.height(20.dp)
                ) {
                    Text(
                        text = "Inicia sesión",
                        color = Color(0xFF00A2E8),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// Sub-componente auxiliar reutilizable para mantener limpia la UI
@Composable
fun CampoTexto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = GrayLight) },
            leadingIcon = { Icon(icon, contentDescription = null, tint = GrayLight) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF59CBA2),
                unfocusedBorderColor = Color(0xFF59CBA2)
            )
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPassword(
    onSaveAndAccessClick: () -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.imagenfondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.20f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // El círculo verde con la palomita blanca de tu diseño
            Image(
                painter = painterResource(id = R.drawable.img_correcto),
                contentDescription = "Icon correct",
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Logotipo y Cabecera de Pillbot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logopastillero),
                    contentDescription = "Logotipo de Pillbot",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "PILLBOT",
                        fontSize = 45.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.Black
                    )
                    Text(
                        text = "Tu dispensador de pastillas\ninteligente",
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                text = "Restablecer tu contraseña",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo 1: Nueva Contraseña
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nueva contraseña",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = { Text("********", color = GrayLight) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GrayLight) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(id = if (passwordVisible) android.R.drawable.ic_menu_view else R.drawable.ic_visibility_off),
                                contentDescription = null,
                                tint = GrayLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF59CBA2),
                        unfocusedBorderColor = Color(0xFF59CBA2)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo 2: Confirmar Nueva Contraseña
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Confirmar nueva contraseña",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("********", color = GrayLight) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GrayLight) },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                painter = painterResource(id = if (confirmPasswordVisible) android.R.drawable.ic_menu_view else R.drawable.ic_visibility_off),
                                contentDescription = null,
                                tint = GrayLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF59CBA2),
                        unfocusedBorderColor = Color(0xFF59CBA2)
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón GUARDAR Y ACCEDER
            Button(
                onClick = { onSaveAndAccessClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF59CBA2)
                )
            ) {
                Text(
                    text = "GUARDAR Y ACCEDER",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificacionCode(
    onVerifyCodeClick: () -> Unit
) {
    val codeValues = remember { mutableStateListOf("", "", "", "", "", "") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.imagenfondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.20f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logopastillero),
                    contentDescription = "Logotipo de Pillbot",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "PILLBOT",
                        fontSize = 45.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.Black
                    )
                    Text(
                        text = "Tu dispensador de pastillas\ninteligente",
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                text = "Verificar código",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Ingresa el código de 6 dígitos enviado a",
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Ejemplo@gmail.com",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GrayLight,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..5) {
                    OutlinedTextField(
                        value = codeValues[i],
                        onValueChange = { input ->
                            if (input.length <= 1 && input.all { it.isDigit() }) {
                                codeValues[i] = input
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(0.75f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { onVerifyCodeClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF59CBA2))
            ) {
                Text(text = "VERIFICAR CÓDIGO", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¿No recibiste el código? Reenviar en 0:59",
                color = Color.Black,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(
    onBackToLogin: () -> Unit,
    onCodeSent: () -> Unit) {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.imagenfondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.20f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logopastillero),
                    contentDescription = "Logotipo de Pillbot",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "PILLBOT",
                        fontSize = 45.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.Black
                    )
                    Text(
                        text = "Tu dispensador de pastillas\ninteligente",
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                text = "Restaurar contraseña",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingresa tu correo electronico registrado.\nTe enviaremos un codigo para\nrestablecer tu contraseña",
                fontSize = 14.sp,
                lineHeight = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Correo electronico",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Ejemplo@gmail.com", color = GrayLight) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = GrayLight) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = GrayLight
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {onCodeSent()},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF59CBA2)
                )
            ) {
                Text(
                    text = "ENVIAR CODIGO",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Volver al inicio de sesión",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { onBackToLogin()}
            )
        }
    }
}