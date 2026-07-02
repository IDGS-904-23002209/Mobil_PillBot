package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class MedicamentoRequest(
    val id_usuario: Int,
    val nombre_medicamento: String,
    val gramaje_medicamento: String,
    val inventario_inicial: Int,
    val descripcion: String
)

data class InventarioMedicamentoRequest(
    @SerializedName("idMedicamento")
    val idMedicamento: Int,

    @SerializedName("lote")
    val lote: String,

    @SerializedName("fechaCaducidad")
    val fechaCaducidad: String, // Se manda como String con la fecha en formato ISO

    @SerializedName("stock")
    val stock: Int,

    @SerializedName("precio")
    val precio: Double
)

// ... (Tus clases existentes quedan igual)

data class CatalogoMedicamentoRequest(
    @SerializedName("nombreComercial")
    val nombreComercial: String,

    @SerializedName("principioActivo")
    val principioActivo: String,

    @SerializedName("idCategoria")
    val idCategoria: Int,

    @SerializedName("idPresentacion")
    val idPresentacion: Int,

    @SerializedName("idUnidadMedida")
    val idUnidadMedida: Int,

    @SerializedName("gramaje")
    val gramaje: String,

    @SerializedName("fabricante")
    val fabricante: String,

    @SerializedName("requiereReceta")
    val requiereReceta: Boolean,

    @SerializedName("activo")
    val activo: Boolean = true
)