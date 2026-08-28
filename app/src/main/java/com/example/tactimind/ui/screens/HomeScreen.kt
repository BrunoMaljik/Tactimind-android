package com.example.tactimind.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tactimind.R
import com.example.tactimind.ui.components.ImageActionCard

@Composable
fun HomeScreen(
    onLolClick: () -> Unit,
    onTftClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Dobrodošli u Tactimind",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Odaberi igru i pronađi savjet za pobjedu.",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        ImageActionCard(
            image = R.drawable.lol_card,
            text = "League of Legends savjeti",
            onClick = onLolClick,
            cardHeight = 170
        )

        Spacer(modifier = Modifier.height(20.dp))

        ImageActionCard(
            image = R.drawable.tft_card,
            text = "TFT savjeti",
            onClick = onTftClick,
            cardHeight = 170
        )

        Spacer(modifier = Modifier.height(28.dp))

        ImageActionCard(
            image = R.drawable.logout_card,
            text = "Odjava",
            onClick = onLogoutClick
        )
    }
}