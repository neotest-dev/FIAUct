package com.neotestdev.fiauct.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.neotestdev.fiauct.data.model.Course

@Composable
fun CourseFormScreen(
    course: Course?,
    programs: List<String>,
    modalities: List<String>,
    cycles: List<String>,
    onSave: (Course) -> Unit
) {
    var codigo by remember { mutableStateOf(course?.codigo ?: "") }
    var curso by remember { mutableStateOf(course?.curso ?: "") }
    var docente by remember { mutableStateOf(course?.docente ?: "") }
    var programa by remember { mutableStateOf(course?.programa ?: programs.firstOrNull().orEmpty()) }
    var modalidad by remember { mutableStateOf(course?.modalidad ?: modalities.firstOrNull().orEmpty()) }
    var ciclo by remember { mutableStateOf(course?.ciclo ?: cycles.firstOrNull().orEmpty()) }
    var modCurso by remember { mutableStateOf(course?.modCurso ?: "") }
    var horas by remember { mutableStateOf(course?.horas?.toString().orEmpty()) }
    var creditos by remember { mutableStateOf(course?.creditos?.toString().orEmpty()) }
    var tipoEstudio by remember { mutableStateOf(course?.tipoEstudio ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(if (course == null) "Nuevo curso" else "Editar curso", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = codigo,
            onValueChange = { if (course == null) codigo = it },
            readOnly = course != null,
            label = { Text("Codigo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(curso, { curso = it }, label = { Text("Curso") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(docente, { docente = it }, label = { Text("Docente") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(programa, { programa = it }, label = { Text("Programa") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(modalidad, { modalidad = it }, label = { Text("Modalidad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(ciclo, { ciclo = it }, label = { Text("Ciclo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(modCurso, { modCurso = it }, label = { Text("Modalidad del curso") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = horas,
            onValueChange = { horas = it },
            label = { Text("Horas") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = creditos,
            onValueChange = { creditos = it },
            label = { Text("Creditos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tipoEstudio,
            onValueChange = { tipoEstudio = it },
            label = { Text("Tipo de estudio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (codigo.isBlank() || curso.isBlank() || docente.isBlank()) return@Button
                onSave(
                    Course(
                        id = course?.id ?: codigo,
                        programa = programa,
                        modalidad = modalidad,
                        ciclo = ciclo,
                        codigo = codigo,
                        curso = curso,
                        docente = docente,
                        modCurso = modCurso.ifBlank { null },
                        horas = horas.toIntOrNull(),
                        tipoEstudio = tipoEstudio.ifBlank { null },
                        creditos = creditos.toIntOrNull(),
                        updatedAt = course?.updatedAt
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}
