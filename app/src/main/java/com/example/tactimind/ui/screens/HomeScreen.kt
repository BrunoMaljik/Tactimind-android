package com.example.tactimind.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onLolClick: () -> Unit,
    onTftClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dobrodošli u Tactimind")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLolClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("League of Legends savjeti")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onTftClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("TFT savjeti")
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Odjava")
        }
    }
}