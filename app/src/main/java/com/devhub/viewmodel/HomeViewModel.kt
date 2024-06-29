package com.devhub.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var username = mutableStateOf<String?>(null)
    var profileImageUrl = mutableStateOf<String?>(null)
    var email = mutableStateOf<String?>(null)

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                try {
                    val userDoc = firestore.collection("users").document(uid).get().await()
                    username.value = userDoc.getString("username")
                    profileImageUrl.value = userDoc.getString("profileImageUrl")
                    email.value = userDoc.getString("email")
                } catch (e: Exception) {
                    println("Error fetching user data: ${e.message}")
                }
            }
        } else {
            println("No user is logged in")
        }
    }
    fun logout() {
        auth.signOut()
    }
}

