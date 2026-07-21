package com.jimenaoropeza.pillbot.pantallas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.data.modelo.CompartimentoRequest

@Composable
fun CompartimentosScreen(
    compartimentos: List<CompartimentoRequest>,
    cargando: Boolean,
    onVolver: () -> Unit,
    onGuardarCantidad: (idCompartimento: Int, nuevaCantidad: Int, onResult: (Boolean, String) -> Unit) -> Unit = { _, _, onResult -> onResult(false, "No configurado") }
) {
    val disponibles = compartimentos.filter {
        it.estado.equals("Disponible", ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onVolver
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = "Compartimentos",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D2A44)
                )
            }

            Text(
                text = "Consulta el estado de los compartimentos de tu dispensador",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (disponibles.isNotEmpty()) {
                        Color(0xFFE8FFF4)
                    } else {
                        Color(0xFFFFEEEE)
                    }
                )
            ) {
                Text(
                    text = if (disponibles.isNotEmpty()) {
                        "${disponibles.size} compartimentos disponibles"
                    } else {
                        "No hay compartimentos disponibles"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = if (disponibles.isNotEmpty()) {
                        Color(0xFF22986F)
                    } else {
                        Color.Red
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (cargando) {
                CircularProgressIndicator(
                    color = Color(0xFF59CBA2)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            if (!cargando && compartimentos.isEmpty()) {
                Text(
                    text = "No se encontraron compartimentos para este usuario.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        items(compartimentos.size) { posicion ->
            val compartimento = compartimentos[posicion]

            CompartimentoCard(
                compartimento = compartimento,
                onGuardarCantidad = onGuardarCantidad
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CompartimentoCard(
    compartimento: CompartimentoRequest,
    onGuardarCantidad: (idCompartimento: Int, nuevaCantidad: Int, onResult: (Boolean, String) -> Unit) -> Unit = { _, _, onResult -> onResult(false, "No configurado") }
) {
    val context = LocalContext.current
    val disponible = compartimento.estado.equals(
        "Disponible",
        ignoreCase = true
    )

    val LIMITE_MAXIMO = 30

    var editando by remember(compartimento.idCompartimento) { mutableStateOf(false) }
    var cantidadEditable by remember(compartimento.idCompartimento, compartimento.cantidadActualPastillas) {
        mutableStateOf(compartimento.cantidadActualPastillas.toString())
    }
    var guardando by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7FDFB)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.logopastillero
                ),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "Compartimento ${compartimento.numeroCompartimento}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0xFF1D2A44)
                )

                Text(
                    text = "Capacidad máxima: ${compartimento.capacidadMaximaPastillas}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                // ---- Cantidad actual: chip compacto, editable al tocar el lápiz ----
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Text(
                        text = "Cantidad actual: ",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    if (editando) {
                        Column {
                            OutlinedTextField(
                                value = cantidadEditable,
                                onValueChange = { nuevoValor ->
                                    if (nuevoValor.length <= 2 && nuevoValor.all { it.isDigit() }) {
                                        val numero = nuevoValor.toIntOrNull()
                                        if (numero == null || numero <= LIMITE_MAXIMO) {
                                            cantidadEditable = nuevoValor
                                        }
                                    }
                                },
                                singleLine = true,
                                enabled = !guardando,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = TextStyle(fontSize = 13.sp, textAlign = TextAlign.Center),
                                modifier = Modifier
                                    .width(56.dp)
                                    .height(40.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF59CBA2),
                                    unfocusedBorderColor = Color(0xFF59CBA2)
                                )
                            )
                            Text(
                                text = "Máx. $LIMITE_MAXIMO",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        if (guardando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = Color(0xFF59CBA2)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Guardar cantidad",
                                tint = Color(0xFF59CBA2),
                                modifier = Modifier
                                    .size(22.dp)
                                    .clickable {
                                        val nuevaCantidad = cantidadEditable.toIntOrNull()
                                        when {
                                            nuevaCantidad == null -> {
                                                Toast.makeText(context, "Ingresa un número válido", Toast.LENGTH_SHORT).show()
                                            }
                                            nuevaCantidad > LIMITE_MAXIMO -> {
                                                Toast.makeText(context, "La cantidad máxima permitida es $LIMITE_MAXIMO", Toast.LENGTH_SHORT).show()
                                            }
                                            else -> {
                                                guardando = true
                                                onGuardarCantidad(compartimento.idCompartimento, nuevaCantidad) { exito, mensaje ->
                                                    guardando = false
                                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                                                    if (exito) {
                                                        editando = false
                                                    }
                                                }
                                            }
                                        }
                                    }
                            )
                        }
                    } else {
                        Text(
                            text = compartimento.cantidadActualPastillas.toString(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D2A44)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar cantidad",
                            tint = Color(0xFF59CBA2),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    cantidadEditable = compartimento.cantidadActualPastillas.toString()
                                    editando = true
                                }
                        )
                    }
                }

                // ---- Medicamento asignado, solo si está ocupado ----
                if (!disponible && !compartimento.nombreMedicamento.isNullOrBlank()) {
                    Text(
                        text = "Medicamento: ${compartimento.nombreMedicamento}",
                        fontSize = 13.sp,
                        color = Color(0xFF1D2A44),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Text(
                text = if (disponible) {
                    "Disponible"
                } else {
                    "Ocupado"
                },
                color = if (disponible) {
                    Color(0xFF59CBA2)
                } else {
                    Color(0xFFFF9800)
                },
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}