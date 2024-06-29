package com.devhub.ui.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.devhub.navigation.SetupNavGraph
import com.devhub.ui.theme.DevHubTheme
import com.devhub.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevHubTheme {
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    if (FirebaseAuth.getInstance().currentUser != null) {
                        navController.navigate("home_screen") {
                            popUpTo("main_screen") { inclusive = true }
                        }
                    }
                }

                SetupNavGraph(navController = navController, loginViewModel = loginViewModel)
            }
        }
    }
}
