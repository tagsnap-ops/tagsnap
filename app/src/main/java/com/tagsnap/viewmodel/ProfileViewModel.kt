package com.tagsnap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tagsnap.data.models.AppUser
import com.tagsnap.data.repositories.UserRepository
import com.tagsnap.data.firebase.FirebaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    fun loadProfile() {
        viewModelScope.launch {
            val uid = FirebaseProvider.auth.currentUser?.uid ?: return@launch
            val user = userRepository.getUser(uid)
            _state.value = _state.value.copy(user = user)
        }
    }
}

data class ProfileState(
    val user: AppUser? = null
)
