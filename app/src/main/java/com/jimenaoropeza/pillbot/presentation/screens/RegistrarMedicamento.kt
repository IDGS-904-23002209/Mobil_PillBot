package com.jimenaoropeza.pillbot.pantallas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.jimenaoropeza.pillbot.viewmodel.MedicamentoViewModel
import com.jimenaoropeza.pillbot.R
import java.io.File
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioMedicamento(
    currentScreen: String,
    totalNoLeidas: Int,
    onNavTabClick: (String) -> Unit,
    viewModel: MedicamentoViewModel
) {
    val context = LocalContext.current

    var procesandoEscaneo by remember { mutableStateOf(false) }
    var textoExtraido by remember { mutableStateOf("") }

    val uriImagenTemporal = remember {
        val directory = File(context.cacheDir, "recetas_fotos")
        directory.mkdirs()
        val file = File.createTempFile("receta_", ".jpg", directory)
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    val recognizer = remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            procesandoEscaneo = true
            try {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uriImagenTemporal)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uriImagenTemporal)
                    ImageDecoder.decodeBitmap(source)
                }

                val image = InputImage.fromBitmap(bitmap, 0)

                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        procesandoEscaneo = false
                        if (visionText.text.isNotBlank()) {
                            textoExtraido = visionText.text
                            Toast.makeText(context, "¡Receta escaneada con éxito!", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, "No se detectó texto claro en la receta.", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        procesandoEscaneo = false
                        Toast.makeText(context, "Error al escanear: ${e.message}", Toast.LENGTH_LONG).show()
                    }

            } catch (e: Exception) {
                procesandoEscaneo = false
                Toast.makeText(context, "Error al cargar la imagen.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF1F1F1),
                tonalElevation = 4.dp
            ) {
                val navIcons = listOf(
                    R.drawable.ic_inicio, R.drawable.ic_formulario, R.drawable.ic_notificacion,
                    R.drawable.ic_inventario, R.drawable.ic_calendario, R.drawable.ic_emergencia, R.drawable.ic_perfil
                )
                val routes = listOf(
                    "inicio", "formulario", "notificaciones", "inventario", "calendario", "controlEmergencia", "perfil"
                )

                navIcons.forEachIndexed { index, iconRes ->
                    val isSelected = currentScreen == routes[index]
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onNavTabClick(routes[index]) },
                        icon = {
                            Box(modifier = Modifier.wrapContentSize()) {
                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                                if (index == 2 && totalNoLeidas > 0) {
                                    Box(
                                        modifier = Modifier.size(16.dp).background(Color.Red, RoundedCornerShape(8.dp)).align(Alignment.TopEnd),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = totalNoLeidas.toString(), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = if (isSelected) Color(0xFF59CBA2).copy(alpha = 0.3f) else Color.Transparent
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.drawable.logopastillero), contentDescription = null, modifier = Modifier.size(55.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "PILLBOT", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Registrar medicamentos", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Agrega un nuevo medicamento al dispensador", color = Color.Gray, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(25.dp))
            Text(text = "¿Cómo deseas agregar tu medicamento?", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Black, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FDFB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_camera), contentDescription = null, modifier = Modifier.size(50.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Escanear receta impresa", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Coloca la receta impresa en una superficie plana y bien iluminada para extraer la información.", fontSize = 12.sp, color = Color.DarkGray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp)
                            .background(Color(0xFFF1F1F1), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (procesandoEscaneo) {
                            CircularProgressIndicator(color = Color(0xFF59CBA2))
                        } else {
                            Text(
                                text = if (textoExtraido.isNotBlank()) "Texto detectado:\n\n$textoExtraido" else "Ninguna imagen escaneada aún",
                                color = if (textoExtraido.isNotBlank()) Color.Black else Color.Gray,
                                fontSize = 13.sp,
                                textAlign = if (textoExtraido.isNotBlank()) TextAlign.Start else TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { cameraLauncher.launch(uriImagenTemporal) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF59CBA2)),
                        enabled = !procesandoEscaneo
                    ) {
                        Text(text = if (textoExtraido.isNotBlank()) "ESCANEAR DE NUEVO" else "ABRIR CÁMARA", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FDFB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_edit), contentDescription = null, modifier = Modifier.size(50.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Ingresar manualmente", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Completa el formulario tradicional con el nombre, los horarios y las dosis del tratamiento.", fontSize = 12.sp, color = Color.DarkGray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onNavTabClick("formularioManual") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF59CBA2))
                    ) {
                        Text(text = "ABRIR FORMULARIO", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}