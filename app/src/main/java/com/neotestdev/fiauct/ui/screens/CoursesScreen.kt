package com.neotestdev.fiauct.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.neotestdev.fiauct.data.model.Course
import com.neotestdev.fiauct.ui.components.CourseCard

@Composable
fun CoursesScreen(
    program: String,
    modality: String,
    cycle: String,
    courses: List<Course>,
    onCourseSelected: (Course) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(
                items = courses,
                key = { "${it.codigo}-${it.modalidad}" } // Clave única para cada curso
            ) { course ->
                CourseCard(course = course, onClick = { onCourseSelected(course) })
            }
        }
    }
}
