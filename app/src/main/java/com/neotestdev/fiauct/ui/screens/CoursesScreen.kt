package com.neotestdev.fiauct.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neotestdev.fiauct.data.model.Course
import com.neotestdev.fiauct.ui.components.CourseCard

@Composable
fun CoursesScreen(courses: List<Course>, onCourseSelected: (Course) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Cursos Disponibles", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(courses) { course ->
                CourseCard(course = course, onClick = { onCourseSelected(course) })
            }
        }
    }
}
