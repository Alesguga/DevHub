package com.devhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            runCatching {
                auth.signInWithEmailAndPassword(email, password)
            }.onSuccess { task ->
                task.addOnCompleteListener { onResult(it.isSuccessful) }
            }.onFailure {
                println("Error al iniciar sesión: ${it.message}")
                onResult(false)
            }
        }
    }

    fun register(email: String, password: String, onResult: (Boolean) -> Unit) {
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
                auth.createUserWithEmailAndPassword(email, password)
            }.onSuccess { task ->
                task.addOnCompleteListener { onResult(it.isSuccessful) }
            }.onFailure {
                println("Error al registrarse: ${it.message}")
                onResult(false)
            }
        }
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
