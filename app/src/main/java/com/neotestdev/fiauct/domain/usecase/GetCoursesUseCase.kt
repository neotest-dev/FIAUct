package com.neotestdev.fiauct.domain.usecase

import com.neotestdev.fiauct.data.model.Course
import com.neotestdev.fiauct.data.repository.CourseRepository
import kotlinx.coroutines.flow.Flow

class GetCoursesUseCase(private val repository: CourseRepository) {
    operator fun invoke(): Flow<List<Course>> = repository.observeCourses()
}
