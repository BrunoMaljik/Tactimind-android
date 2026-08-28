package com.example.tactimind.data

import com.google.firebase.auth.FirebaseAuth

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    fun register(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(
                        false,
                        task.exception?.message ?: "Registracija nije uspjela."
                    )
                }
            }
    }

    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(
                        false,
                        task.exception?.message ?: "Prijava nije uspjela."
                    )
                }
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}