package com.neotestdev.fiauct.data.model

import com.google.gson.annotations.SerializedName

data class Course(
    val programa: String,
    val modalidad: String,
    val ciclo: String,
    val codigo: String,
    val curso: String,
    val docente: String,
    @SerializedName("mod-curso")
    val modCurso: String? = null,
    val horas: Int? = null,
    val tipoEstudio: String? = null,
    val creditos: Int? = null
)
