package com.neotestdev.fiauct.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotestdev.fiauct.data.model.Course
import com.neotestdev.fiauct.data.repository.CourseRepository
import com.neotestdev.fiauct.domain.usecase.GetCoursesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {

    private val repository = CourseRepository()
    private val getCoursesUseCase = GetCoursesUseCase(repository)
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow<CourseUiState>(CourseUiState.Loading)
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    private val _writeState = MutableStateFlow<WriteState>(WriteState.Idle)
    val writeState: StateFlow<WriteState> = _writeState.asStateFlow()

    val messageEvents = MutableSharedFlow<String>()

    fun loadCourses() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = CourseUiState.Loading
            try {
                getCoursesUseCase().collect { courses ->
                    if (courses.isNotEmpty()) {
                        _uiState.value = CourseUiState.Success(courses, courses.size)
                    } else {
                        _uiState.value = CourseUiState.Error("No se pudieron cargar los cursos")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = CourseUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun resetWriteState() {
        _writeState.value = WriteState.Idle
    }

    fun saveCourse(course: Course) {
        viewModelScope.launch {
            _writeState.value = WriteState.Saving
            try {
                repository.upsertCourse(course)
                _writeState.value = WriteState.Success
            } catch (e: Exception) {
                val message = e.message ?: "No se pudo guardar el curso"
                _writeState.value = WriteState.Error(message)
                messageEvents.emit(message)
            }
        }
    }

    fun deleteCourse(codigo: String) {
        viewModelScope.launch {
            _writeState.value = WriteState.Saving
            try {
                repository.deleteCourse(codigo)
                _writeState.value = WriteState.Success
            } catch (e: Exception) {
                val message = e.message ?: "No se pudo eliminar el curso"
                _writeState.value = WriteState.Error(message)
                messageEvents.emit(message)
            }
        }
    }
}
