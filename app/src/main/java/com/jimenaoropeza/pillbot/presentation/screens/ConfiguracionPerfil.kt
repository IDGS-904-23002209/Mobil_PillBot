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
import com.jimenaoropeza.pillbot.R

val AzulPillbot = Color(0xFF2298D4)
val VerdePillbot = Color(0xFF59CBA2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionPerfil(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {

    var nombre by remember { mutableStateOf("Usuario López Martínez") }
    var correo by remember { mutableStateOf("usuario@gmail.com") }
    var telefono by remember { mutableStateOf("477-123-4567") }

    var contacto1 by remember { mutableStateOf("Juan Perez Cortes") }
    var telefono1 by remember { mutableStateOf("477-890-1234") }

    var contacto2 by remember { mutableStateOf("Maria Garcia Padilla") }
    var telefono2 by remember { mutableStateOf("477-567-8901") }

    var passwordActual by remember { mutableStateOf("") }
    var passwordNueva by remember { mutableStateOf("") }

    Scaffold(

        bottomBar = {

            NavigationBar(
                containerColor = Color(0xFFF1F1F1)
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

                            Box {

                                Image(
                                    painter = painterResource(iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )

                                if (index == 2 && totalNoLeidas > 0) {

                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(
                                                Color.Red,
                                                CircleShape
                                            )
                                            .align(Alignment.TopEnd),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Text(
                                            text = totalNoLeidas.toString(),
                                            color = Color.White,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor =
                                if (isSelected)
                                    VerdePillbot.copy(alpha = 0.3f)
                                else
                                    Color.Transparent
                        )
                    )
                }
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

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Configuración de perfil",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Administra tu información",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD9D9D9)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier.size(90.dp)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.ic_usuario),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )

                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(30.dp)
                                .offset(x = 8.dp, y = 8.dp)
                                .align(Alignment.BottomEnd)
                                .background(
                                    color = Color.White,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 2.dp,
                                    color = VerdePillbot,
                                    shape = CircleShape
                                )
                        )
                        {
                            Image(
                                painter = painterResource(id = R.drawable.ic_camera),
                                contentDescription = "Cambiar foto",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {

                        Text(
                            text = "Usuario",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = correo,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TituloSeccion("Información personal")

            CampoSimple(
                titulo = "Nombre completo",
                valor = nombre
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                CampoSimple(
                    titulo = "Correo electrónico",
                    valor = correo,
                    modifier = Modifier.weight(1f)
                )

                CampoSimple(
                    titulo = "Número telefónico",
                    valor = telefono,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TituloSeccion("Contactos de emergencia")

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                CampoSimple(
                    titulo = "Nombre completo",
                    valor = contacto1,
                    modifier = Modifier.weight(1f)
                )

                CampoSimple(
                    titulo = "Número telefónico",
                    valor = telefono1,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                CampoSimple(
                    titulo = "Nombre completo",
                    valor = contacto2,
                    modifier = Modifier.weight(1f)
                )

                CampoSimple(
                    titulo = "Número telefónico",
                    valor = telefono2,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TituloSeccion("Seguridad")

            CampoPassword(
                titulo = "Ingresa tu contraseña actual",
                valor = passwordActual,
                onValueChange = {
                    passwordActual = it
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoPassword(
                titulo = "Ingresa tu contraseña nueva",
                valor = passwordNueva,
                onValueChange = {
                    passwordNueva = it
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePillbot
                )
            ) {

                Text("Guardar Cambios")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCerrarSesion,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF356799)
                )
            ) {

                Text("Cerrar Sesión")
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun TituloSeccion(texto: String) {

    Text(
        text = texto,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = AzulPillbot,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun CampoSimple(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {

    Column(modifier) {

        Text(
            text = titulo,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdePillbot,
                unfocusedBorderColor = VerdePillbot
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

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = titulo,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdePillbot,
                unfocusedBorderColor = VerdePillbot
            )
        )
    }
}