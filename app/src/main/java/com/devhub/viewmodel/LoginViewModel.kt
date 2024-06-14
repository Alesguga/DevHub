package com.devhub.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            runCatching {
                auth.signInWithEmailAndPassword(email, password).await()
            }.onSuccess {
                onResult(true)
            }.onFailure {
                println("Error al iniciar sesión: ${it.message}")
                onResult(false)
            }
        }
    }

    fun register(email: String, password: String, username: String, profileImageUri: Uri, onResult: (Boolean) -> Unit) {
        if (!isEmailValid(email)) {
            onResult(false)
            println("Email no válido")
            return
        }
        if (!isPasswordValid(password)) {
            onResult(false)
            println("Contraseña no válida")
            return
        }

        viewModelScope.launch {
            runCatching {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = authResult.user?.uid ?: throw Exception("No se pudo obtener el UID del usuario")
                val imageUrl = uploadProfileImage(uid, profileImageUri)
                saveUserToFirestore(uid, username, email, imageUrl)

            }.onSuccess {
                onResult(true)
            }.onFailure {
                println("Error al registrarse: ${it.message}")
                onResult(false)
            }
        }
    }

    private suspend fun uploadProfileImage(uid: String, imageUri: Uri): String {
        val storageRef = storage.reference.child("profile_images/$uid.jpg")
        storageRef.putFile(imageUri).await()
        return storageRef.downloadUrl.await().toString()
    }

    private suspend fun saveUserToFirestore(uid: String, username: String, email: String, imageUrl: String) {
        val user = hashMapOf(
            "username" to username,
            "email" to email,
            "profileImageUrl" to imageUrl
        )
        firestore.collection("users").document(uid).set(user).await()
    }

    fun isEmailValid(email: String): Boolean {
        val validDomains = listOf("gmail.com", "hotmail.com", "protonmail.com", "yahoo.com")
        val domain = email.substringAfter("@")
        return domain in validDomains
    }

    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$")
        return password.matches(passwordRegex)
    }
}
