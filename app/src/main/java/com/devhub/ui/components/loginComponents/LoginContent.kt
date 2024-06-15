package com.devhub.ui.components.loginComponents

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.devhub.ui.components.LogoText
import com.devhub.ui.theme.Blue1
import com.devhub.ui.theme.Gray1
import com.devhub.viewmodel.LoginViewModel

@Composable
fun MainContent(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val isDarkTheme = isSystemInDarkTheme()
    val primaryColor = if (isDarkTheme) Color.White else Gray1
    val secondaryColor = Blue1
    var isRegistering by remember { mutableStateOf(true) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LogoText(primaryColor = primaryColor, secondaryColor = secondaryColor)

            Spacer(modifier = Modifier.height(100.dp))

            if (isRegistering) {
                RegisterForm(navController, loginViewModel, primaryColor, secondaryColor) {
                    isRegistering = false
                }
            } else {
                LoginForm(navController, loginViewModel, primaryColor, secondaryColor) {
                    isRegistering = true
                }
            }
        }
    }
}
