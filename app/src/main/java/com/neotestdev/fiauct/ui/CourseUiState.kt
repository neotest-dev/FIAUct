package com.neotestdev.fiauct.ui

import com.neotestdev.fiauct.data.model.Course

sealed class CourseUiState {
    object Loading : CourseUiState()
    data class Success(val courses: List<Course>) : CourseUiState()
    data class Error(val message: String) : CourseUiState()
}
