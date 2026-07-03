package com.jimenaoropeza.pillbot.data.modelo

import com.google.gson.annotations.SerializedName

data class Tratamiento(

    @SerializedName("idTratamiento")
    val id_tratamiento: Int = 0,

    @SerializedName("nombreTratamiento")
    val nombre_tratamiento: String,

    @SerializedName("fechaInicio")
    val fecha_inicio: String,

    @SerializedName("fechaFin")
    val fecha_fin: String?
)