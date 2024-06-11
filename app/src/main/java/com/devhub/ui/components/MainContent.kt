package com.devhub.ui.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.devhub.R
import com.devhub.ui.components.LogoText
import com.devhub.ui.theme.Blue1
import com.devhub.ui.theme.Gray1
import com.devhub.viewmodel.LoginViewModel

@Composable
fun MainContent(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val isDarkTheme = isSystemInDarkTheme()
    val primaryColor = if (isDarkTheme) Color.White else Gray1
    val secondaryColor = Blue1
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

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

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Gmail") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password"
                    )
                },
                trailingIcon = {
                    val imageRes = if (passwordVisibility) R.drawable.ojoa else R.drawable.ojoc
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Show/Hide Password",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.medium
            )

            Button(
                onClick = {
                    loginViewModel.login(emailState.value, passwordState.value) { isSuccess ->
                        if (isSuccess) {
                            navController.navigate("home_screen")
                            Toast.makeText(navController.context, "Login Correcto", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(navController.context, "Login Fallido", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "Iniciar sesión",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(
                onClick = {
                    loginViewModel.register(emailState.value, passwordState.value) { isSuccess ->
                        if (isSuccess) {
                            navController.navigate("home_screen")
                            Toast.makeText(navController.context, "Cuenta Registrada", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(navController.context, "Registro Fallido", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "Registrarse",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

