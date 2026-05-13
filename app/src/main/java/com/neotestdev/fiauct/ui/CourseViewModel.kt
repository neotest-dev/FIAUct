package com.neotestdev.fiauct.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotestdev.fiauct.data.repository.CourseRepository
import com.neotestdev.fiauct.domain.usecase.GetCoursesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {

    // Sin Hilt todavía, instanciamos aquí (o idealmente pasar por constructor)
    private val repository = CourseRepository()
    private val getCoursesUseCase = GetCoursesUseCase(repository)

    private val _uiState = MutableStateFlow<CourseUiState>(CourseUiState.Loading)
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    fun loadCourses(context: Context) {
        viewModelScope.launch {
            _uiState.value = CourseUiState.Loading
            try {
                val courses = getCoursesUseCase(context)
                if (courses.isNotEmpty()) {
                    _uiState.value = CourseUiState.Success(courses)
                } else {
                    _uiState.value = CourseUiState.Error("No se pudieron cargar los cursos")
                }
            } catch (e: Exception) {
                _uiState.value = CourseUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
