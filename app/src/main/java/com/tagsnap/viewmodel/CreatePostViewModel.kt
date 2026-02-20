package com.tagsnap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tagsnap.data.models.Post
import com.tagsnap.data.models.PostType
import com.tagsnap.data.repositories.PostRepository
import com.tagsnap.data.firebase.FirebaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreatePostViewModel(
    private val postRepository: PostRepository = PostRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(CreatePostState())
    val state: StateFlow<CreatePostState> = _state

    fun updateText(text: String) {
        _state.value = _state.value.copy(text = text)
    }

    fun updateType(type: PostType) {
        _state.value = _state.value.copy(type = type)
    }

    fun addMediaUrl(url: String) {
        if (url.isBlank()) return
        _state.value = _state.value.copy(mediaUrls = _state.value.mediaUrls + url)
    }

    fun removeMediaUrl(index: Int) {
        _state.value = _state.value.copy(
            mediaUrls = _state.value.mediaUrls.toMutableList().also { list ->
                if (index in list.indices) list.removeAt(index)
            }
        )
    }

    fun updatePollOption(index: Int, value: String) {
        _state.value = _state.value.copy(
            pollOptions = _state.value.pollOptions.toMutableList().also { list ->
                if (index in list.indices) list[index] = value
            }
        )
    }

    fun addPollOption() {
        _state.value = _state.value.copy(pollOptions = _state.value.pollOptions + "")
    }

    fun removePollOption(index: Int) {
        _state.value = _state.value.copy(
            pollOptions = _state.value.pollOptions.toMutableList().also { list ->
                if (index in list.indices) list.removeAt(index)
            }
        )
    }

    fun submit() {
        viewModelScope.launch {
            val user = FirebaseProvider.auth.currentUser ?: return@launch
            postRepository.createPost(
                Post(
                    authorId = user.uid,
                    authorHandle = user.email?.substringBefore("@") ?: "user",
                    type = _state.value.type,
                    text = _state.value.text,
                    mediaUrls = _state.value.mediaUrls,
                    pollOptions = _state.value.pollOptions.filter { it.isNotBlank() }
                )
            )
            _state.value = CreatePostState(success = true)
        }
    }
}

data class CreatePostState(
    val text: String = "",
    val type: PostType = PostType.TEXT,
    val mediaUrls: List<String> = emptyList(),
    val pollOptions: List<String> = listOf("", ""),
    val success: Boolean = false
)
