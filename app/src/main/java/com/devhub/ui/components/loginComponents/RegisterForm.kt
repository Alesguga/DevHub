package com.devhub.ui.components.loginComponents

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import coil.compose.rememberAsyncImagePainter
import com.devhub.R
import com.devhub.viewmodel.LoginViewModel
@Composable
fun RegisterForm(
    navController: NavController,
    loginViewModel: LoginViewModel,
    primaryColor: Color,
    secondaryColor: Color,
    onSwitchToLogin: () -> Unit
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val usernameState = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        profileImageUri = it
    }

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
        value = usernameState.value,
        onValueChange = { usernameState.value = it },
        label = { Text("Nombre de Usuario") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.medium
    )

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
        onClick = { imageLauncher.launch("image/*") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(
            "Seleccionar Imagen de Perfil",
            style = MaterialTheme.typography.bodyLarge
        )
    }
    profileImageUri?.let {
        Image(
            painter = rememberAsyncImagePainter(it),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
    }

    Button(
        onClick = {
            if (profileImageUri != null) {
                loginViewModel.register(emailState.value, passwordState.value, usernameState.value, profileImageUri!!) { isSuccess ->
                    if (isSuccess) {
                        navController.navigate("home_screen")
                        Toast.makeText(navController.context, "Cuenta Registrada", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(navController.context, "Registro Fallido", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(navController.context, "Selecciona una imagen de perfil", Toast.LENGTH_SHORT).show()
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

    Spacer(modifier = Modifier.height(16.dp))

    ClickableText(
        text = AnnotatedString("¿Ya tienes una cuenta? Inicia sesión aquí"),
        onClick = { onSwitchToLogin() },
        style = MaterialTheme.typography.bodySmall.copy(color = secondaryColor)
    )
}
