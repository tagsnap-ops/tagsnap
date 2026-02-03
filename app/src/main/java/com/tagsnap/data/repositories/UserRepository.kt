package com.tagsnap.data.repositories

import com.google.firebase.Timestamp
import com.tagsnap.data.firebase.FirebaseProvider
import com.tagsnap.data.models.AppUser
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseProvider.firestore

    suspend fun createUserProfile(user: AppUser) {
        firestore.collection("users").document(user.uid).set(
            mapOf(
                "uid" to user.uid,
                "handle" to user.handle,
                "email" to user.email,
                "createdAt" to Timestamp.now(),
                "reputationPoints" to 0,
                "badges" to emptyList<String>(),
                "isShadowBanned" to false,
                "lastActiveAt" to Timestamp.now(),
                "avatarUrl" to ""
            )
        ).await()
    }

    suspend fun getUser(uid: String): AppUser? {
        val snapshot = firestore.collection("users").document(uid).get().await()
        val data = snapshot.data ?: return null
        return AppUser(
            uid = data["uid"] as? String ?: uid,
            handle = data["handle"] as? String ?: "",
            email = data["email"] as? String ?: "",
            createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now(),
            reputationPoints = (data["reputationPoints"] as? Long)?.toInt() ?: 0,
            badges = data["badges"] as? List<String> ?: emptyList(),
            isShadowBanned = data["isShadowBanned"] as? Boolean ?: false,
            lastActiveAt = data["lastActiveAt"] as? Timestamp ?: Timestamp.now(),
            avatarUrl = data["avatarUrl"] as? String ?: ""
        )
    }
}
