package com.jimenaoropeza.pillbot.modelo

import com.google.gson.annotations.SerializedName

data class MedicamentoRequest(
    val idTratamiento: Int = 0,
    val idMedicamento: Int = 0,
    val nombreComercial: String,
    val principioActivo: String,
    val cantidadActualPastillas: Int = 0,
    val numeroCompartimento: Int = 0,
    val estadoCompartimento: String = "",
    val idCategoria: Int,
    val idPresentacion: Int,
    val idUnidadMedida: Int,
    val gramaje: String,
    val fabricante: String,
    val requiereReceta: Boolean,
    val activo: Boolean = true,
    val idUsuario: Int
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
    val fabricante: String? = null,

    @SerializedName("requiereReceta")
    val requiereReceta: Boolean,

    @SerializedName("activo")
    val activo: Boolean = true
)

