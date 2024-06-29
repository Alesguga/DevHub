package com.devhub.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devhub.ui.views.HomeScreen
import com.devhub.ui.components.loginComponents.MainContent
import com.devhub.viewmodel.HomeViewModel
import com.devhub.viewmodel.LoginViewModel

@Composable
fun SetupNavGraph(navController: NavHostController, loginViewModel: LoginViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(route = Screen.Main.route) {
            MainContent(navController = navController, loginViewModel = loginViewModel)
        }
        composable(route = Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(navController = navController, homeViewModel = homeViewModel)
        }
    }
}

sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Home : Screen("home_screen")
}
