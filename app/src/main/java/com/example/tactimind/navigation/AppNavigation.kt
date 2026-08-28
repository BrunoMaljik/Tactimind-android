package com.example.tactimind.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tactimind.ui.screens.HomeScreen
import com.example.tactimind.ui.screens.LoginScreen
import com.example.tactimind.ui.screens.LolScreen
import com.example.tactimind.ui.screens.RegisterScreen
import com.example.tactimind.ui.screens.TftScreen
import com.example.tactimind.viewmodel.AuthViewModel

private object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val LOL = "lol"
    const val TFT = "tft"
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()

    val startDestination = remember {
        if (authViewModel.isUserLoggedIn()) {
            Routes.HOME
        } else {
            Routes.LOGIN
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                isLoading = authViewModel.isLoading,
                errorMessage = authViewModel.errorMessage,
                onLoginClick = { email, password ->
                    authViewModel.login(email, password) {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                        }
                    }
                },
                onRegisterClick = {
                    authViewModel.clearError()
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                isLoading = authViewModel.isLoading,
                errorMessage = authViewModel.errorMessage,
                onRegisterClick = { email, password, repeatedPassword ->
                    authViewModel.register(
                        email = email,
                        password = password,
                        repeatedPassword = repeatedPassword
                    ) {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                        }
                    }
                },
                onBackToLoginClick = {
                    authViewModel.clearError()
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onLolClick = {
                    navController.navigate(Routes.LOL)
                },
                onTftClick = {
                    navController.navigate(Routes.TFT)
                },
                onLogoutClick = {
                    authViewModel.logout()

                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.LOL) {
            LolScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.TFT) {
            TftScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}