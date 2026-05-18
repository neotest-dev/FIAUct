package com.neotestdev.fiauct.domain.usecase

import android.content.Context
import com.neotestdev.fiauct.data.model.Course
import com.neotestdev.fiauct.data.repository.CourseRepository

class GetCoursesUseCase(private val repository: CourseRepository) {

    suspend operator fun invoke(context: Context): List<Course> {
        // Lógica de negocio: Carga estrictamente local desde assets
        return repository.loadCoursesFromAssets(context)
    }
}
