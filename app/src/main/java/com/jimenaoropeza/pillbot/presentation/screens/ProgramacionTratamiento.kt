package com.jimenaoropeza.pillbot.presentation.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jimenaoropeza.pillbot.data.modelo.CompartimentoRequest
import com.jimenaoropeza.pillbot.data.modelo.DetalleRecetaCompletoRequest
import com.jimenaoropeza.pillbot.modelo.ProgramacionTratamientoRequest
import com.jimenaoropeza.pillbot.presentation.viewmodel.CompartimentoViewModel
import com.jimenaoropeza.pillbot.presentation.viewmodel.DetalleRecetaCompletoViewModel
import com.jimenaoropeza.pillbot.presentation.viewmodel.ProgramacionViewModel
import androidx.compose.material.icons.automirrored.filled.ArrowBack

private val COLOR_PRINCIPAL = Color(0xFF59CBA2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarTratamiento(
    usuarioId: Int,   // NUEVO
    onVolver: () -> Unit,
    onGuardadoExitoso: () -> Unit = onVolver
) {
    val context = LocalContext.current

    val programacionViewModel: ProgramacionViewModel = viewModel()
    val detalleRecetaViewModel: DetalleRecetaCompletoViewModel = viewModel()
    val compartimentoViewModel: CompartimentoViewModel = viewModel()

    var formularioAbierto by remember { mutableStateOf(false) }   // NUEVO: estado subido desde SeccionRegistrarTratamiento

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // ---- Flecha para volver al inicio ---- NUEVO
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver al inicio",
                    tint = COLOR_PRINCIPAL
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        SeccionRegistrarTratamiento(
            context = context,
            usuarioId = usuarioId,   // NUEVO
            programacionViewModel = programacionViewModel,
            detalleRecetaViewModel = detalleRecetaViewModel,
            compartimentoViewModel = compartimentoViewModel,
            onGuardadoExitoso = onGuardadoExitoso,
            formularioAbierto = formularioAbierto,                          // NUEVO
            onFormularioAbiertoChange = { formularioAbierto = it }          // NUEVO
        )

        Spacer(modifier = Modifier.height(24.dp))

        // NUEVO: la tabla solo se muestra cuando el formulario de registro está cerrado
        if (!formularioAbierto) {
            SeccionTablaYEditarTratamientos(
                context = context,
                usuarioId = usuarioId,   // NUEVO
                programacionViewModel = programacionViewModel,
                detalleRecetaViewModel = detalleRecetaViewModel,
                compartimentoViewModel = compartimentoViewModel,
                onGuardadoExitoso = onGuardadoExitoso
            )
        }
    }
}

// ============================================================================
// SECCIÓN: REGISTRAR (crear un tratamiento nuevo)
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeccionRegistrarTratamiento(
    context: android.content.Context,
    usuarioId: Int,   // NUEVO

    programacionViewModel: ProgramacionViewModel,
    detalleRecetaViewModel: DetalleRecetaCompletoViewModel,
    compartimentoViewModel: CompartimentoViewModel,
    onGuardadoExitoso: () -> Unit,
    formularioAbierto: Boolean,                          // NUEVO: ahora viene del padre
    onFormularioAbiertoChange: (Boolean) -> Unit          // NUEVO: ahora viene del padre
) {
    val detalles by detalleRecetaViewModel.detallesCompletos
    val compartimentos by compartimentoViewModel.compartimentos
    val cargandoDetalles by detalleRecetaViewModel.cargando
    val cargandoCompartimentos by compartimentoViewModel.cargando

    var refreshTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(refreshTrigger) {
        if (formularioAbierto) {
            detalleRecetaViewModel.cargarTodosLosDetalles(soloDisponibles = true, idUsuario = usuarioId) { mensaje ->
                Toast.makeText(context, "Detalle receta: $mensaje", Toast.LENGTH_LONG).show()
            }
            compartimentoViewModel.cargarCompartimentosUsuario(usuarioId) { mensaje ->
                Toast.makeText(context, "Compartimentos: $mensaje", Toast.LENGTH_LONG).show()
            }
        }
    }

    var detalleSeleccionado by remember { mutableStateOf<DetalleRecetaCompletoRequest?>(null) }
    var compartimentoSeleccionado by remember { mutableStateOf<CompartimentoRequest?>(null) }
    var horaInicio by remember { mutableStateOf("") }
    var diaInicio by remember { mutableStateOf("") }
    //var pesoEsperado by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }
    var guardando by remember { mutableStateOf(false) }

    fun limpiar() {
        detalleSeleccionado = null
        compartimentoSeleccionado = null
        horaInicio = ""
        diaInicio = ""
       // pesoEsperado = ""
        activo = true
    }

    if (!formularioAbierto) {
        Button(
            onClick = { onFormularioAbiertoChange(true)
                refreshTrigger++},
                modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = COLOR_PRINCIPAL)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text("Registrar Tratamiento")
        }
    }

    AnimatedVisibility(
        visible = formularioAbierto,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Registrar tratamiento",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = COLOR_PRINCIPAL
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    CamposTratamiento(
                        detalles = detalles,
                        compartimentos = compartimentos,
                        cargandoDetalles = cargandoDetalles,
                        cargandoCompartimentos = cargandoCompartimentos,
                        detalleSeleccionado = detalleSeleccionado,
                        onDetalleSeleccionado = { detalleSeleccionado = it },
                        compartimentoSeleccionado = compartimentoSeleccionado,
                        onCompartimentoSeleccionado = { compartimentoSeleccionado = it },
                        horaInicio = horaInicio,
                        onHoraInicioChange = { horaInicio = it },
                        diaInicio = diaInicio,
                        onDiaInicioChange = { diaInicio = it },
                      //  pesoEsperado = pesoEsperado,
                     //   onPesoEsperadoChange = { pesoEsperado = it },
                        activo = activo,
                        onActivoChange = { activo = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        limpiar()
                        onFormularioAbiertoChange(false)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.size(12.dp))

                Button(
                    onClick = {
                        when {
                            detalleSeleccionado == null -> {
                                Toast.makeText(context, "Selecciona un tratamiento", Toast.LENGTH_SHORT).show()
                            }
                            compartimentoSeleccionado == null -> {
                                Toast.makeText(context, "Selecciona un compartimento", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                val request = ProgramacionTratamientoRequest(
                                    idDetalleReceta = detalleSeleccionado!!.idDetalleReceta,
                                    idCompartimento = compartimentoSeleccionado!!.idCompartimento,
                                    horaInicio = horaInicio.trim().ifBlank { null },
                                   // pesoEsperadoGramos = pesoEsperado.toDoubleOrNull(),
                                    diaInicio = diaInicio.trim().ifBlank { null },
                                    diaFin = null,
                                    activo = activo
                                )

                                guardando = true
                                programacionViewModel.registrarProgramacion(request) { exito, mensaje, _ ->
                                    guardando = false
                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                                    if (exito) {
                                        limpiar()
                                        onFormularioAbiertoChange(false)
                                        onGuardadoExitoso()
                                    }
                                }
                            }
                        }
                    },
                    enabled = !guardando,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = COLOR_PRINCIPAL)
                ) {
                    Text(if (guardando) "Guardando..." else "Guardar tratamiento")
                }
            }
        }
    }
}

// ============================================================================
// SECCIÓN: TABLA DE TRATAMIENTOS ACTIVOS + EDICIÓN POR RENGLÓN
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeccionTablaYEditarTratamientos(
    context: android.content.Context,
    usuarioId: Int,   // NUEVO
    programacionViewModel: ProgramacionViewModel,
    detalleRecetaViewModel: DetalleRecetaCompletoViewModel,
    compartimentoViewModel: CompartimentoViewModel,
    onGuardadoExitoso: () -> Unit
) {
    val tratamientos by programacionViewModel.tratamientos
    val cargandoTratamientos = programacionViewModel.cargando

    val detalles by detalleRecetaViewModel.detallesCompletos
    val compartimentos by compartimentoViewModel.compartimentos
    val cargandoCompartimentos by compartimentoViewModel.cargando

    // Se cargan los datos apenas se muestra la pantalla (no hace falta abrir nada).
    // soloDisponibles = false para poder mostrar/editar el detalle_receta ya asignado.
    LaunchedEffect(Unit) {
        programacionViewModel.cargarTratamientos(usuarioId)   // <-- se agrega el parámetro
        detalleRecetaViewModel.cargarTodosLosDetalles(soloDisponibles = false, idUsuario = usuarioId) { mensaje ->
            Toast.makeText(context, "Detalle receta: $mensaje", Toast.LENGTH_LONG).show()
        }
        compartimentoViewModel.cargarCompartimentosUsuario(usuarioId) { mensaje ->
            Toast.makeText(context, "Compartimentos: $mensaje", Toast.LENGTH_LONG).show()
        }
    }

    var tratamientoEditando by remember { mutableStateOf<ProgramacionTratamientoRequest?>(null) }

    val tratamientosActivos = tratamientos.filter { it.activo == true }

    Text(
        text = "Tratamientos activos",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = COLOR_PRINCIPAL
    )
    Spacer(modifier = Modifier.height(8.dp))

    when {
        cargandoTratamientos -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text("Cargando tratamientos...", color = Color.Gray)
            }
        }
        tratamientosActivos.isEmpty() -> {
            Text("No hay tratamientos activos registrados.", color = Color.Gray)
        }
        else -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    // ---- Encabezado ----
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("N° Trat.", modifier = Modifier.width(70.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp) // antes: "Detalle"
                        Text("Padecimiento", modifier = Modifier.width(140.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Medicamento", modifier = Modifier.width(140.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Dosis", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Indicaciones", modifier = Modifier.width(160.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Frec. (hrs)", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Duración (días)", modifier = Modifier.width(110.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Compart.", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Hora inicio", modifier = Modifier.width(90.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Día inicio", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Día fin", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Editar", modifier = Modifier.width(60.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // ---- Renglones ----
                    tratamientosActivos.forEach { tratamiento ->
                        val compartimento = compartimentos.find { it.idCompartimento == tratamiento.idCompartimento }
                        val detalleInfo = detalles.find { it.idDetalleReceta == tratamiento.idDetalleReceta }   // NUEVO

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = detalleInfo?.numeroTratamiento?.toString() ?: "-",   // antes: tratamiento.idDetalleReceta?.toString() ?: "-"
                                modifier = Modifier.width(70.dp),
                                fontSize = 13.sp
                            )
                            Text(
                                text = detalleInfo?.padecimiento ?: "-",
                                modifier = Modifier.width(140.dp),
                                fontSize = 13.sp
                            )
                            Text(
                                text = detalleInfo?.nombreComercial ?: "-",
                                modifier = Modifier.width(140.dp),
                                fontSize = 13.sp
                            )
                            Text(
                                text = detalleInfo?.dosis ?: "-",
                                modifier = Modifier.width(100.dp),
                                fontSize = 13.sp
                            )
                            Text(
                                text = detalleInfo?.indicaciones ?: "-",
                                modifier = Modifier.width(160.dp),
                                fontSize = 13.sp
                            )
                            Text(
                                text = detalleInfo?.frecuenciaHoras?.toString() ?: "-",
                                modifier = Modifier.width(80.dp),
                                fontSize = 13.sp
                            )
                            Text(
                                text = detalleInfo?.duracionDias?.toString() ?: "-",
                                modifier = Modifier.width(110.dp),
                                fontSize = 13.sp
                            )
                            Text(
                                text = compartimento?.numeroCompartimento?.toString() ?: tratamiento.idCompartimento?.toString() ?: "-",
                                modifier = Modifier.width(80.dp),
                                fontSize = 13.sp
                            )

                            // ... el resto del renglón queda exactamente igual
                            Text(
                                text = tratamiento.horaInicio ?: "-",
                                modifier = Modifier.width(90.dp),
                                fontSize = 13.sp
                            )
                            Text(
                                text = formatearFecha(tratamiento.diaInicio),
                                modifier = Modifier.width(100.dp),
                                fontSize = 13.sp
                            )
                            Text(
                                text = formatearFecha(tratamiento.diaFin),
                                modifier = Modifier.width(100.dp),
                                fontSize = 13.sp
                            )
                            IconButton(
                                onClick = { tratamientoEditando = tratamiento },
                                modifier = Modifier.width(60.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = COLOR_PRINCIPAL
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFF0F0F0))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }

    // ---- Formulario de edición: aparece debajo de la tabla al presionar "Editar" ----
    AnimatedVisibility(
        visible = tratamientoEditando != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        tratamientoEditando?.let { tratamiento ->
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                FormularioEditarTratamiento(
                    context = context,
                    tratamiento = tratamiento,
                    detalles = detalles,
                    compartimentos = compartimentos,
                    cargandoCompartimentos = cargandoCompartimentos,
                    programacionViewModel = programacionViewModel,
                    onCancelar = { tratamientoEditando = null },
                    onGuardadoExitoso = {
                        tratamientoEditando = null
                        onGuardadoExitoso()
                    }
                )
            }
        }
    }
}

// ============================================================================
// FORMULARIO DE EDICIÓN (id_detalle_receta fijo, el resto editable)
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormularioEditarTratamiento(
    context: android.content.Context,
    tratamiento: ProgramacionTratamientoRequest,
    detalles: List<DetalleRecetaCompletoRequest>,
    compartimentos: List<CompartimentoRequest>,
    cargandoCompartimentos: Boolean,
    programacionViewModel: ProgramacionViewModel,
    onCancelar: () -> Unit,
    onGuardadoExitoso: () -> Unit
) {
    // El detalle de receta queda fijo: es el mismo que se asignó al registrar.
    val detalleFijo = remember(tratamiento.idProgramacion, detalles) {
        detalles.find { it.idDetalleReceta == tratamiento.idDetalleReceta }
    }

    var compartimentoSeleccionado by remember(tratamiento.idProgramacion) {
        mutableStateOf<CompartimentoRequest?>(null)
    }
    LaunchedEffect(compartimentos, tratamiento.idProgramacion) {
        if (compartimentoSeleccionado == null) {
            compartimentoSeleccionado = compartimentos.find { it.idCompartimento == tratamiento.idCompartimento }
        }
    }

    var horaInicio by remember(tratamiento.idProgramacion) { mutableStateOf(tratamiento.horaInicio ?: "") }
    var diaInicio by remember(tratamiento.idProgramacion) {
        mutableStateOf(formatearFecha(tratamiento.diaInicio).let { if (it == "-") "" else it })
    }
    //var pesoEsperado by remember(tratamiento.idProgramacion) { mutableStateOf(tratamiento.pesoEsperadoGramos?.toString() ?: "") }
    var activo by remember(tratamiento.idProgramacion) { mutableStateOf(tratamiento.activo ?: true) }
    var guardando by remember { mutableStateOf(false) }
    var expandedCompartimento by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Editar tratamiento",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = COLOR_PRINCIPAL
            )
            Spacer(modifier = Modifier.height(16.dp))

            // ---- Detalle del tratamiento: FIJO, no editable ----
            Text("Detalle del tratamiento (no editable)", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))
            CampoSoloLectura(
                etiqueta = "Número de tratamiento",                                          // antes: "Id detalle receta"
                valor = detalleFijo?.numeroTratamiento?.toString() ?: "-"                    // antes: tratamiento.idDetalleReceta?.toString() ?: "-"
            )

            if (detalleFijo != null) {
                Spacer(modifier = Modifier.height(8.dp))
                CampoSoloLectura(etiqueta = "Padecimiento", valor = detalleFijo.padecimiento ?: "-")
                Spacer(modifier = Modifier.height(8.dp))
                CampoSoloLectura(etiqueta = "Medicamento", valor = detalleFijo.nombreComercial ?: "-")
                Spacer(modifier = Modifier.height(8.dp))
                CampoSoloLectura(etiqueta = "Dosis", valor = detalleFijo.dosis ?: "-")
                Spacer(modifier = Modifier.height(8.dp))
                CampoSoloLectura(etiqueta = "Indicaciones", valor = detalleFijo.indicaciones ?: "-")
                Spacer(modifier = Modifier.height(8.dp))
                CampoSoloLectura(etiqueta = "Frecuencia (horas)", valor = detalleFijo.frecuenciaHoras?.toString() ?: "-")
                Spacer(modifier = Modifier.height(8.dp))
                CampoSoloLectura(etiqueta = "Duración (días)", valor = detalleFijo.duracionDias?.toString() ?: "-")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ---- Compartimento: editable ----
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Número de compartimento", modifier = Modifier.weight(1f))
                if (cargandoCompartimentos) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text(text = "(${compartimentos.size})", fontSize = 12.sp, color = Color.Gray)
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandedCompartimento,
                onExpandedChange = { expandedCompartimento = it }
            ) {
                OutlinedTextField(
                    value = compartimentoSeleccionado?.numeroCompartimento?.toString() ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Selecciona un compartimento") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCompartimento) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = COLOR_PRINCIPAL,
                        unfocusedBorderColor = COLOR_PRINCIPAL
                    )
                )

                ExposedDropdownMenu(
                    expanded = expandedCompartimento,
                    onDismissRequest = { expandedCompartimento = false }
                ) {
                    if (compartimentos.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No hay compartimentos disponibles") },
                            onClick = { expandedCompartimento = false },
                            enabled = false
                        )
                    } else {
                        compartimentos.forEach { compartimento ->
                            DropdownMenuItem(
                                text = { Text(compartimento.numeroCompartimento.toString()) },
                                onClick = {
                                    compartimentoSeleccionado = compartimento
                                    expandedCompartimento = false
                                }
                            )
                        }
                    }
                }
            }

            compartimentoSeleccionado?.let { compartimento ->
                Spacer(modifier = Modifier.height(10.dp))
                CampoSoloLectura(etiqueta = "Estado", valor = compartimento.estado ?: "-")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Programación", color = COLOR_PRINCIPAL, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Text("Hora de inicio")
            OutlinedTextField(
                value = horaInicio,
                onValueChange = { horaInicio = it },
                placeholder = { Text("08:00:00") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = COLOR_PRINCIPAL,
                    unfocusedBorderColor = COLOR_PRINCIPAL
                )
            )

            Spacer(modifier = Modifier.height(12.dp))



            Spacer(modifier = Modifier.height(12.dp))

            Text("Día de inicio")
            Text(text = "Formato: aaaa-mm-dd", fontSize = 11.sp, color = Color.Gray)
            OutlinedTextField(
                value = diaInicio,
                onValueChange = { diaInicio = it },
                placeholder = { Text("2026-07-07") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = COLOR_PRINCIPAL,
                    unfocusedBorderColor = COLOR_PRINCIPAL
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Día de fin")
            Text(text = "Se calcula automáticamente (día de inicio + duración de la receta)", fontSize = 11.sp, color = Color.Gray)
            OutlinedTextField(
                value = "Se calcula automáticamente",
                onValueChange = {},
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = COLOR_PRINCIPAL,
                    disabledTextColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Activo", modifier = Modifier.weight(1f))
                Switch(checked = activo, onCheckedChange = { activo = it })
            }
        }
    }

    // ---- Botones debajo del formulario ----
    Spacer(modifier = Modifier.height(16.dp))

    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = onCancelar,
            modifier = Modifier.weight(1f)
        ) {
            Text("Cancelar")
        }

        Spacer(modifier = Modifier.size(12.dp))

        Button(
            onClick = {
                when {
                    compartimentoSeleccionado == null -> {
                        Toast.makeText(context, "Selecciona un compartimento", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        val idProgramacion = tratamiento.idProgramacion!!
                        val request = ProgramacionTratamientoRequest(
                            idProgramacion = idProgramacion,
                            idDetalleReceta = tratamiento.idDetalleReceta, // fijo: no cambia
                            idCompartimento = compartimentoSeleccionado!!.idCompartimento,
                            horaInicio = horaInicio.trim().ifBlank { null },
                           // pesoEsperadoGramos = pesoEsperado.toDoubleOrNull(),
                            diaInicio = diaInicio.trim().ifBlank { null },
                            diaFin = null,
                            activo = activo
                        )

                        guardando = true
                        programacionViewModel.actualizarProgramacion(idProgramacion, request) { exito, mensaje ->
                            guardando = false
                            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                            if (exito) {
                                onGuardadoExitoso()
                            }
                        }
                    }
                }
            },
            enabled = !guardando,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = COLOR_PRINCIPAL)
        ) {
            Text(if (guardando) "Actualizando..." else "Actualizar tratamiento")
        }
    }
}

// ============================================================================
// CAMPOS COMPARTIDOS (usados solo por Registrar)
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CamposTratamiento(
    detalles: List<DetalleRecetaCompletoRequest>,
    compartimentos: List<CompartimentoRequest>,
    cargandoDetalles: Boolean,
    cargandoCompartimentos: Boolean,
    detalleSeleccionado: DetalleRecetaCompletoRequest?,
    onDetalleSeleccionado: (DetalleRecetaCompletoRequest) -> Unit,
    compartimentoSeleccionado: CompartimentoRequest?,
    onCompartimentoSeleccionado: (CompartimentoRequest) -> Unit,
    horaInicio: String,
    onHoraInicioChange: (String) -> Unit,
    diaInicio: String,
    onDiaInicioChange: (String) -> Unit,
    // pesoEsperado: String,
   // onPesoEsperadoChange: (String) -> Unit,
    activo: Boolean,
    onActivoChange: (Boolean) -> Unit
) {
    var expandedDetalle by remember { mutableStateOf(false) }
    var expandedCompartimento by remember { mutableStateOf(false) }

    // ---- DROPDOWN: ID Detalle Receta ----
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Detalle del tratamiento", modifier = Modifier.weight(1f))
        if (cargandoDetalles) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        } else {
            Text(text = "(${detalles.size})", fontSize = 12.sp, color = Color.Gray)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expandedDetalle,
        onExpandedChange = { expandedDetalle = it }
    ) {
        OutlinedTextField(
            value = detalleSeleccionado?.numeroTratamiento?.toString() ?: "",   // antes: idDetalleReceta
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Selecciona un número de tratamiento") },       // texto actualizado
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDetalle) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = COLOR_PRINCIPAL,
                unfocusedBorderColor = COLOR_PRINCIPAL
            )
        )

        ExposedDropdownMenu(
            expanded = expandedDetalle,
            onDismissRequest = { expandedDetalle = false }
        ) {
            if (detalles.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No hay detalles de receta disponibles") },
                    onClick = { expandedDetalle = false },
                    enabled = false
                )
            } else {
                detalles.forEach { detalle ->
                    DropdownMenuItem(
                        text = { Text(detalle.numeroTratamiento?.toString() ?: "-") },   // antes: idDetalleReceta.toString()
                        onClick = {
                            onDetalleSeleccionado(detalle)   // esto NO cambia: sigue guardando por idDetalleReceta
                            expandedDetalle = false
                        }
                    )
                }
            }
        }
    }

    detalleSeleccionado?.let { detalle ->
        Spacer(modifier = Modifier.height(10.dp))
        CampoSoloLectura(etiqueta = "Padecimiento", valor = detalle.padecimiento ?: "-")
        Spacer(modifier = Modifier.height(8.dp))
        CampoSoloLectura(etiqueta = "Medicamento", valor = detalle.nombreComercial ?: "-")
        Spacer(modifier = Modifier.height(8.dp))
        CampoSoloLectura(etiqueta = "Dosis", valor = detalle.dosis ?: "-")
        Spacer(modifier = Modifier.height(8.dp))
        CampoSoloLectura(etiqueta = "Indicaciones", valor = detalle.indicaciones ?: "-")
        Spacer(modifier = Modifier.height(8.dp))
        CampoSoloLectura(etiqueta = "Frecuencia (horas)", valor = detalle.frecuenciaHoras?.toString() ?: "-")
        Spacer(modifier = Modifier.height(8.dp))
        CampoSoloLectura(etiqueta = "Duración (días)", valor = detalle.duracionDias?.toString() ?: "-")
    }

    Spacer(modifier = Modifier.height(16.dp))

    // ---- DROPDOWN: Número de compartimento ----
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Número de compartimento", modifier = Modifier.weight(1f))
        if (cargandoCompartimentos) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        } else {
            Text(text = "(${compartimentos.size})", fontSize = 12.sp, color = Color.Gray)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expandedCompartimento,
        onExpandedChange = { expandedCompartimento = it }
    ) {
        OutlinedTextField(
            value = compartimentoSeleccionado?.numeroCompartimento?.toString() ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Selecciona un compartimento") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCompartimento) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = COLOR_PRINCIPAL,
                unfocusedBorderColor = COLOR_PRINCIPAL
            )
        )

        ExposedDropdownMenu(
            expanded = expandedCompartimento,
            onDismissRequest = { expandedCompartimento = false }
        ) {
            if (compartimentos.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No hay compartimentos disponibles") },
                    onClick = { expandedCompartimento = false },
                    enabled = false
                )
            } else {
                compartimentos.forEach { compartimento ->
                    DropdownMenuItem(
                        text = { Text(compartimento.numeroCompartimento.toString()) },
                        onClick = {
                            onCompartimentoSeleccionado(compartimento)
                            expandedCompartimento = false
                        }
                    )
                }
            }
        }
    }

    compartimentoSeleccionado?.let { compartimento ->
        Spacer(modifier = Modifier.height(10.dp))
        CampoSoloLectura(etiqueta = "Estado", valor = compartimento.estado ?: "-")
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(text = "Programación", color = COLOR_PRINCIPAL, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    Spacer(modifier = Modifier.height(12.dp))

    Text("Hora de inicio")
    Text(text = "Si la dejas vacía, el servidor usa la hora actual", fontSize = 11.sp, color = Color.Gray)
    OutlinedTextField(
        value = horaInicio,
        onValueChange = onHoraInicioChange,
        placeholder = { Text("08:00:00 (opcional)") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = COLOR_PRINCIPAL,
            unfocusedBorderColor = COLOR_PRINCIPAL
        )
    )

    Spacer(modifier = Modifier.height(12.dp))


    Spacer(modifier = Modifier.height(12.dp))

    Text("Día de inicio")
    Text(text = "Si lo dejas vacío, el servidor usa la fecha actual", fontSize = 11.sp, color = Color.Gray)
    OutlinedTextField(
        value = diaInicio,
        onValueChange = onDiaInicioChange,
        placeholder = { Text("2026-07-07 (opcional)") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = COLOR_PRINCIPAL,
            unfocusedBorderColor = COLOR_PRINCIPAL
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text("Día de fin")
    OutlinedTextField(
        value = "Se calcula automáticamente",
        onValueChange = {},
        readOnly = true,
        enabled = false,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = COLOR_PRINCIPAL,
            disabledTextColor = Color.Gray
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Activo", modifier = Modifier.weight(1f))
        Switch(checked = activo, onCheckedChange = onActivoChange)
    }
}

@Composable
private fun CampoSoloLectura(etiqueta: String, valor: String) {
    Column {
        Text(text = etiqueta, fontSize = 12.sp, color = Color.Gray)
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color(0xFF59CBA2),
                disabledTextColor = Color.Black
            )
        )
    }
}

/**
 * Toma una fecha en formato ISO (p.ej. "2026-07-07" o "2026-07-07T00:00:00")
 * y devuelve solo la parte "aaaa-mm-dd". Si viene vacía o nula, devuelve "-".
 */
private fun formatearFecha(fecha: String?): String {
    if (fecha.isNullOrBlank()) return "-"
    return if (fecha.length >= 10) fecha.substring(0, 10) else fecha
}