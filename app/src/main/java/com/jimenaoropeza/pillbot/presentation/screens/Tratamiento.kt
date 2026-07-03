package com.jimenaoropeza.pillbot.pantallas

import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jimenaoropeza.pillbot.data.modelo.Tratamiento
import com.jimenaoropeza.pillbot.presentation.viewmodel.TratamientoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tratamiento(
    onVolver: () -> Unit
) {

    val context = LocalContext.current

    val viewModel: TratamientoViewModel = viewModel()

    var nombreTratamiento by remember {
        mutableStateOf("")
    }

    var fechaInicio by remember {
        mutableStateOf("")
    }

    var fechaFin by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onVolver
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar"
                )

            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Tratamiento",
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
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Información del tratamiento",
                    color = Color(0xFF59CBA2),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Nombre del tratamiento")

                OutlinedTextField(
                    value = nombreTratamiento,
                    onValueChange = {
                        nombreTratamiento = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Fecha de inicio")

                OutlinedTextField(
                    value = fechaInicio,
                    onValueChange = {
                        fechaInicio = it
                    },
                    placeholder = {
                        Text("2026-07-02")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Fecha de fin")

                OutlinedTextField(
                    value = fechaFin,
                    onValueChange = {
                        fechaFin = it
                    },
                    placeholder = {
                        Text("2026-08-02")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(

                    onClick = {

                        val tratamiento = Tratamiento(

                            nombre_tratamiento = nombreTratamiento,

                            fecha_inicio = fechaInicio,

                            fecha_fin = if (fechaFin.isBlank()) null else fechaFin

                        )

                        viewModel.registrarTratamiento(
                            tratamiento
                        ) { correcto, mensaje ->

                            if (correcto) {

                                Toast.makeText(
                                    context,
                                    "Registrado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()

                                nombreTratamiento = ""
                                fechaInicio = ""
                                fechaFin = ""

                            } else {

                                Toast.makeText(
                                    context,
                                    mensaje,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }

                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF59CBA2)
                    )

                ) {

                    Text(
                        text = "Registrar tratamiento"
                    )

                }

            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        // Aquí podrás agregar el segundo formulario posteriormente

    }

}