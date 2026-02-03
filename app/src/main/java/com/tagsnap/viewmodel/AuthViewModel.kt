package com.tagsnap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tagsnap.data.models.AppUser
import com.tagsnap.data.repositories.AuthRepository
import com.tagsnap.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                authRepository.login(email, password)
                _uiState.value = _uiState.value.copy(loading = false, success = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(loading = false, error = e.message)
            }
        }
    }

    fun register(email: String, password: String, handle: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                val user = authRepository.register(email, password)
                if (user != null) {
                    userRepository.createUserProfile(
                        AppUser(uid = user.uid, handle = handle, email = email)
                    )
                    authRepository.sendVerification()
                }
                _uiState.value = _uiState.value.copy(loading = false, success = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(loading = false, error = e.message)
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                authRepository.resetPassword(email)
                _uiState.value = _uiState.value.copy(message = "Reset email sent")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun sendVerification() {
        viewModelScope.launch {
            try {
                authRepository.sendVerification()
                _uiState.value = _uiState.value.copy(message = "Verification sent")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}

data class AuthUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
