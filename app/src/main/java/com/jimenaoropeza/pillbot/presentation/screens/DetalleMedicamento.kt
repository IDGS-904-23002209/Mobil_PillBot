package com.jimenaoropeza.pillbot.pantallas

import androidx.compose.material3.Switch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jimenaoropeza.pillbot.modelo.Medicamento
import com.jimenaoropeza.pillbot.R
import com.jimenaoropeza.pillbot.modelo.CatalogoMedicamentoRequest
import com.jimenaoropeza.pillbot.viewmodel.MedicamentoViewModel
import androidx.compose.runtime.*
import com.jimenaoropeza.pillbot.modelo.Categoria
import com.jimenaoropeza.pillbot.modelo.TipoPresentacion
import com.jimenaoropeza.pillbot.modelo.UnidadMedida
import com.jimenaoropeza.pillbot.repository.MedicamentoRepository
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenuItem
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMedicamento(
    medicamento: Medicamento,
    onVolver: () -> Unit,
    onRecargarClick: () -> Unit
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onVolver
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Detalle del medicamento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_pildora),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "${medicamento.nombre_medicamento} ${medicamento.gramaje_medicamento}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(text = "Cantidad actual: ${medicamento.inventario_actual} unidades")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Compartimiento: A1")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Próxima toma: 10:00 AM")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text =
                            if(medicamento.inventario_actual > 5)
                                "Stock suficiente"
                            else
                                "Stock bajo",
                        color =
                            if(medicamento.inventario_actual > 5)
                                Color(0xFF59CBA2)
                            else
                                Color(0xFFFF9800),
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    onRecargarClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF59CBA2)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Recargar medicamento")
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecargarMedicamento(
    medicamento: Medicamento,
    onVolver: () -> Unit
){

    val viewModel: MedicamentoViewModel = viewModel()
    var cantidad by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var lote by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState())
    ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onVolver
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar al inventario",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logopastillero),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "PILLBOT",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Recargar medicamento",
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
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_pildora),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "${medicamento.nombre_medicamento} ${medicamento.gramaje_medicamento}",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Stock suficiente",
                            color = Color(0xFF59CBA2)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Información de la recarga",
                        color = Color(0xFF59CBA2),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Cantidad agregada")

                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { cantidad = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Fecha de caducidad")

                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Lote (opcional)")

                    OutlinedTextField(
                        value = lote,
                        onValueChange = { lote = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Notas (opcional)")

                    OutlinedTextField(
                        value = notas,
                        onValueChange = { notas = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF59CBA2),
                            unfocusedBorderColor = Color(0xFF59CBA2)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF59CBA2)
                )
            ) {
                Text("Registrar recarga")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
