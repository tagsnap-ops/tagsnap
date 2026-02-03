package com.tagsnap.data.repositories

import com.google.firebase.auth.FirebaseUser
import com.tagsnap.data.firebase.FirebaseProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseProvider.auth

    val authState: Flow<FirebaseUser?> = callbackFlow {
        val listener = com.google.firebase.auth.FirebaseAuth.AuthStateListener {
            trySend(it.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun login(email: String, password: String): FirebaseUser? {
        return auth.signInWithEmailAndPassword(email, password).await().user
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        return auth.createUserWithEmailAndPassword(email, password).await().user
    }

    suspend fun sendVerification() {
        auth.currentUser?.sendEmailVerification()?.await()
    }

    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    fun signOut() {
        auth.signOut()
    }

    fun currentUser(): FirebaseUser? = auth.currentUser
}
