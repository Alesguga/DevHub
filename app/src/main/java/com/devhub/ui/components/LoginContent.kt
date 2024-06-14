package com.devhub.ui.views

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
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
    val usernameState = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        profileImageUri = it
    }

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
                    .padding(vertical = 8.dp)
            ) {
                Text("Seleccionar Imagen de Perfil")
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
        }
    }
}
