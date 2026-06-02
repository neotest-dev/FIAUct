package com.neotestdev.fiauct.data.model

data class Course(
    val id: String? = null,
    val programa: String,
    val modalidad: String,
    val ciclo: String,
    val codigo: String,
    val curso: String,
    val docente: String,
    val modCurso: String? = null,
    val horas: Int? = null,
    val tipoEstudio: String? = null,
    val creditos: Int? = null,
    val updatedAt: Long? = null
)
