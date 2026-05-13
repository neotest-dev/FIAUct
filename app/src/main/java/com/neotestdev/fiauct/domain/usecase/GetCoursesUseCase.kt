package com.neotestdev.fiauct.domain.usecase

import android.content.Context
import com.neotestdev.fiauct.data.model.Course
import com.neotestdev.fiauct.data.repository.CourseRepository

class GetCoursesUseCase(private val repository: CourseRepository) {

    suspend operator fun invoke(context: Context): List<Course> {
        // Lógica de negocio: Intentar red, si falla usar local
        val remoteCourses = repository.fetchCoursesFromNetwork()
        return if (remoteCourses.isNotEmpty()) {
            remoteCourses
        } else {
            repository.loadCoursesFromAssets(context)
        }
    }
}
