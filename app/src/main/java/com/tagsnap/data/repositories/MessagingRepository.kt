package com.tagsnap.data.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.tagsnap.data.firebase.FirebaseProvider
import com.tagsnap.data.models.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MessagingRepository {
    private val firestore = FirebaseProvider.firestore

    fun getMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection("messages")
            .document(chatId)
            .collection("items")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.map { doc ->
                    val data = doc.data.orEmpty()
                    Message(
                        messageId = doc.id,
                        senderId = data["senderId"] as? String ?: "",
                        text = data["text"] as? String ?: "",
                        createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now(),
                        readBy = data["readBy"] as? List<String> ?: emptyList()
                    )
                }.orEmpty()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }

    suspend fun sendMessage(chatId: String, message: Message) {
        val doc = firestore.collection("messages").document(chatId).collection("items").document()
        doc.set(
            mapOf(
                "senderId" to message.senderId,
                "text" to message.text,
                "createdAt" to Timestamp.now(),
                "readBy" to message.readBy
            )
        ).await()
    }
}
