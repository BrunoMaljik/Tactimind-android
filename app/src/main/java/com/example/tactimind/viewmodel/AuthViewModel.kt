package com.example.tactimind.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tactimind.data.AuthRepository

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun register(
        email: String,
        password: String,
        repeatedPassword: String,
        onSuccess: () -> Unit
    ) {
        errorMessage = null

        if (email.isBlank() || password.isBlank() || repeatedPassword.isBlank()) {
            errorMessage = "Sva polja moraju biti popunjena."
            return
        }

        if (password != repeatedPassword) {
            errorMessage = "Lozinke se ne podudaraju."
            return
        }

        if (password.length < 6) {
            errorMessage = "Lozinka mora imati najmanje 6 znakova."
            return
        }

        isLoading = true

        repository.register(email.trim(), password) { success, message ->
            isLoading = false

            if (success) {
                onSuccess()
            } else {
                errorMessage = message
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        errorMessage = null

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Unesi e-mail i lozinku."
            return
        }

        isLoading = true

        repository.login(email.trim(), password) { success, message ->
            isLoading = false

            if (success) {
                onSuccess()
            } else {
                errorMessage = message
            }
        }
    }

    fun logout() {
        repository.logout()
        errorMessage = null
    }

    fun isUserLoggedIn(): Boolean {
        return repository.isUserLoggedIn()
    }

    fun clearError() {
        errorMessage = null
    }
}