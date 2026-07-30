package com.jimenaoropeza.pillbot.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.modelo.HistorialMedicamento
import com.jimenaoropeza.pillbot.pantallas.formatearFechaHistorial

@Composable
fun DetalleHistorialMedicamento(
    medicamento: HistorialMedicamento,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Detalle del medicamento",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D2A44)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF7FDFB)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_pildora
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                CampoDetalle(
                    etiqueta = "Medicamento",
                    valor = medicamento.nombreMedicamento
                )

                CampoDetalle(
                    etiqueta = "Principio activo",
                    valor = medicamento.principioActivo
                )

                CampoDetalle(
                    etiqueta = "Padecimiento",
                    valor = medicamento.padecimiento
                )

                CampoDetalle(
                    etiqueta = "Fecha de inicio",
                    valor = formatearFechaHistorial(
                        medicamento.fechaInicio
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Volver")
        }
    }
}

@Composable
fun CampoDetalle(
    etiqueta: String,
    valor: String
) {
    Text(
        text = etiqueta,
        fontSize = 12.sp,
        color = Color.Gray
    )

    Text(
        text = valor,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF1D2A44)
    )

    Spacer(modifier = Modifier.height(14.dp))
}