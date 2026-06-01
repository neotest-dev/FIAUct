package com.neotestdev.fiauct.data.repository

import com.neotestdev.fiauct.data.model.Course
import com.neotestdev.fiauct.data.remote.CourseRemoteDataSource
import com.neotestdev.fiauct.data.remote.FirestoreCourseDataSource
import kotlinx.coroutines.flow.Flow

@Suppress("unused")
class CourseRepository(
    private val remoteDataSource: CourseRemoteDataSource = FirestoreCourseDataSource()
) {
    fun observeCourses(): Flow<List<Course>> = remoteDataSource.observeCourses()
}
