package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.data.modelo.CompartimentoRequest

@Composable
fun CompartimentosScreen(
    compartimentos: List<CompartimentoRequest>,
    cargando: Boolean,
    onVolver: () -> Unit
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
                compartimento = compartimento
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
    compartimento: CompartimentoRequest
) {
    val disponible = compartimento.estado.equals(
        "Disponible",
        ignoreCase = true
    )

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

                Text(
                    text = "Cantidad actual: ${compartimento.cantidadActualPastillas}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
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