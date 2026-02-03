package com.tagsnap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tagsnap.data.models.Message
import com.tagsnap.data.repositories.MessagingRepository
import com.tagsnap.data.firebase.FirebaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessagesViewModel(
    private val messagingRepository: MessagingRepository = MessagingRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(MessagesState())
    val state: StateFlow<MessagesState> = _state

    fun observeChat(chatId: String) {
        viewModelScope.launch {
            messagingRepository.getMessages(chatId).collect { messages ->
                _state.value = _state.value.copy(messages = messages)
            }
        }
    }

    fun sendMessage(chatId: String, text: String) {
        viewModelScope.launch {
            val userId = FirebaseProvider.auth.currentUser?.uid ?: return@launch
            messagingRepository.sendMessage(
                chatId,
                Message(senderId = userId, text = text)
            )
        }
    }
}

data class MessagesState(
    val messages: List<Message> = emptyList()
)
