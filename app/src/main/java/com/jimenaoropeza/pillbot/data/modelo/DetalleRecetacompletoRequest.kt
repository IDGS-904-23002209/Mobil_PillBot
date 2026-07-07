package com.jimenaoropeza.pillbot.data.modelo

import com.google.gson.annotations.SerializedName

/**
 * Representa el "join" de DetalleReceta + Receta + Medicamentos.
 * Se usa SOLO para mostrar información de solo lectura al elegir un id_detalle_receta,
 * y para poder guardar la ProgramacionTratamiento usando el idDetalleReceta.
 *
 * El backend debe exponer esto en un endpoint tipo:
 * GET api/detalleReceta/completo
 *
 * Sugerencia de consulta SQL para el backend:
 *
 * SELECT
 *     dr.id_detalle_receta AS idDetalleReceta,
 *     r.padecimiento        AS padecimiento,
 *     m.nombre_comercial    AS nombreComercial,
 *     dr.dosis              AS dosis,
 *     dr.indicaciones       AS indicaciones,
 *     dr.frecuencia_horas   AS frecuenciaHoras,
 *     dr.duracion_dias      AS duracionDias
 * FROM DetalleReceta dr
 * JOIN Recetas r        ON dr.id_receta = r.id_receta
 * JOIN Medicamentos m   ON dr.id_medicamento = m.id_medicamento
 */
data class DetalleRecetaCompletoRequest(
    @SerializedName("idDetalleReceta") val idDetalleReceta: Int,
    @SerializedName("padecimiento") val padecimiento: String?,
    @SerializedName("nombreComercial") val nombreComercial: String?,
    @SerializedName("dosis") val dosis: String?,
    @SerializedName("indicaciones") val indicaciones: String?,
    @SerializedName("frecuenciaHoras") val frecuenciaHoras: Int?,
    @SerializedName("duracionDias") val duracionDias: Int?
)