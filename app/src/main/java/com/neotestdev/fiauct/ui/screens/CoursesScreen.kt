package com.neotestdev.fiauct.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.neotestdev.fiauct.data.model.Course
import com.neotestdev.fiauct.ui.components.CourseCard
import java.text.Normalizer

@Composable
fun CoursesScreen(
    program: String,
    modality: String,
    cycle: String,
    courses: List<Course>,
    isAdmin: Boolean,
    onEditCourse: (Course) -> Unit,
    onDeleteCourse: (Course) -> Unit,
    onCourseSelected: (Course) -> Unit
) {
    var query by remember(program, modality, cycle) { mutableStateOf("") }

    val normalizedQuery = remember(query) { normalizeText(query) }
    val searchableCourses = remember(courses) {
        courses.map { course ->
            val searchableText = normalizeText(
                listOf(
                    course.curso,
                    course.codigo,
                    course.docente,
                    course.modalidad,
                    course.modCurso.orEmpty()
                ).joinToString(separator = " ")
            )
            course to searchableText
        }
    }
    val visibleCourses = remember(searchableCourses, normalizedQuery) {
        if (normalizedQuery.isBlank()) {
            searchableCourses.map { it.first }
        } else {
            searchableCourses
                .asSequence()
                .filter { (_, searchableText) -> searchableText.contains(normalizedQuery) }
                .map { (course, _) -> course }
                .toList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
        Text(
            text = "$program > $modality > Ciclo $cycle",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        Text(
            text = "Cursos Disponibles",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Buscar curso, docente o codigo") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(
                items = visibleCourses,
                key = { "${it.codigo}-${it.modalidad}" },
                contentType = { "course-card" }
            ) { course ->
                val onClick = remember(course) { { onCourseSelected(course) } }
                val onEdit = remember(course, isAdmin) {
                    if (isAdmin) ({ onEditCourse(course) }) else null
                }
                val onDelete = remember(course, isAdmin) {
                    if (isAdmin) ({ onDeleteCourse(course) }) else null
                }
                CourseCard(
                    course = course,
                    onClick = onClick,
                    onEdit = onEdit,
                    onDelete = onDelete
                )
            }
        }
    }
}

private fun normalizeText(value: String): String {
    val normalized = Normalizer.normalize(value.lowercase(), Normalizer.Form.NFD)
    return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
}
