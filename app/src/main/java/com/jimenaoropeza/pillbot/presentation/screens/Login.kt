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
    viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), // <-- Agregamos el ViewModel aquí
    onBackToLoginClick: () -> Unit,
    onRegisterSuccessClick: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Imagen de fondo con opacidad de Pillbot
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

            // Título de la pantalla
            Text(
                text = "¡Crea tu cuenta!",
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo 1: Nombre Completo
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nombre completo",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Juan Pérez", color = GrayLight) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = GrayLight
                        )
                    },
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

            // Campo 2: Nombre de usuario o correo
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nombre de usuario o correo",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    placeholder = { Text("Ejemplo@gmail.com", color = GrayLight) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = GrayLight
                        )
                    },
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

            // Campo 3: Contraseña
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Contraseña",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { contrasenia = it },
                    placeholder = { Text("********", color = GrayLight) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = GrayLight
                        )
                    },
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

            Spacer(modifier = Modifier.height(32.dp))

            // Botón REGISTRARSE
            if (viewModel.isLoading) {
                CircularProgressIndicator(color = Color(0xFF59CBA2), modifier = Modifier.padding(16.dp))
            }

            viewModel.errorMessage?.let { error ->
                Text(text = error, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
            }

            Button(
                onClick = {
                    if (nombre.isNotEmpty() && correo.isNotEmpty() && contrasenia.isNotEmpty()) {
                        viewModel.registrarUsuario(nombre, correo, contrasenia) {
                            onRegisterSuccessClick() // Redirige al login o pantalla de éxito al registrarse
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
                    text = "REGISTRARSE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Enlace para volver si ya tiene cuenta
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
                        color = Color(0xFF00A2E8), // Color azul de tus enlaces
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
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