package com.neotestdev.fiauct.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotestdev.fiauct.data.repository.CourseRepository
import com.neotestdev.fiauct.domain.usecase.GetCoursesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {

    private val repository = CourseRepository()
    private val getCoursesUseCase = GetCoursesUseCase(repository)
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow<CourseUiState>(CourseUiState.Loading)
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    fun loadCourses() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = CourseUiState.Loading
            try {
                getCoursesUseCase().collect { courses ->
                    if (courses.isNotEmpty()) {
                        _uiState.value = CourseUiState.Success(courses)
                    } else {
                        _uiState.value = CourseUiState.Error("No se pudieron cargar los cursos")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = CourseUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
