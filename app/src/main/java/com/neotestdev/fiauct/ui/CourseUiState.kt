package com.neotestdev.fiauct.ui

import com.neotestdev.fiauct.data.model.Course

sealed class CourseUiState {
    object Loading : CourseUiState()
    data class Success(val courses: List<Course>, val total: Int) : CourseUiState()
    data class Error(val message: String) : CourseUiState()
}

sealed class WriteState {
    object Idle : WriteState()
    object Saving : WriteState()
    object Success : WriteState()
    data class Error(val message: String) : WriteState()
}
