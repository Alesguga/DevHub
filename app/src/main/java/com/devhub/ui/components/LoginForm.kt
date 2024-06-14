package com.devhub.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devhub.R
import com.devhub.viewmodel.LoginViewModel

@Composable
fun LoginForm(
    navController: NavController,
    loginViewModel: LoginViewModel,
    primaryColor: Color,
    secondaryColor: Color,
    onSwitchToRegister: () -> Unit
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = emailState.value,
        onValueChange = {
            emailState.value = it
            emailError = !loginViewModel.isEmailValid(it)
        },
        label = { Text("Email") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email"
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        isError = emailError,
        shape = MaterialTheme.shapes.medium
    )
    if (emailError) {
        Text(
            text = "Email no válido. Usa Gmail, Hotmail, ProtonMail, Yahoo.",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
            passwordError = !loginViewModel.isPasswordValid(it)
        },
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
        isError = passwordError,
        shape = MaterialTheme.shapes.medium
    )
    if (passwordError) {
        Text(
            text = "La contraseña debe tener al menos 8 caracteres, incluyendo letras, números y símbolos.",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }

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
            .padding(vertical = 12.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            "Iniciar sesión",
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    ClickableText(
        text = AnnotatedString("¿No tienes una cuenta? Regístrate aquí"),
        onClick = { onSwitchToRegister() },
        style = MaterialTheme.typography.bodySmall.copy(color = secondaryColor)
    )
}