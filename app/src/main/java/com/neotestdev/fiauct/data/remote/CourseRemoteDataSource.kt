package com.neotestdev.fiauct.data.remote

import com.neotestdev.fiauct.data.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRemoteDataSource {
    fun observeCourses(): Flow<List<Course>>
}
