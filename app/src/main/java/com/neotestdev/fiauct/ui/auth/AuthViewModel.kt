package com.neotestdev.fiauct.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotestdev.fiauct.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val isAdmin: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.currentUser.collect { uid ->
                if (uid == null) {
                    _uiState.value = AuthUiState(isSignedIn = false, isAdmin = false)
                } else {
                    val isAdmin = runCatching { repository.isCurrentUserAdmin() }.getOrDefault(false)
                    _uiState.value = AuthUiState(isSignedIn = true, isAdmin = isAdmin)
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            runCatching { repository.signIn(email, password) }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "No se pudo iniciar sesion"
                    )
                }
        }
    }

    fun signOut() {
        repository.signOut()
    }
}
