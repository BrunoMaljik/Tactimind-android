package com.example.tactimind.ui.screens

import androidx.compose.runtime.Composable
import com.example.tactimind.viewmodel.TipViewModel

@Composable
fun TftScreen(
    tipViewModel: TipViewModel,
    onBackClick: () -> Unit
) {
    TipScreen(
        game = "TFT",
        screenTitle = "TFT savjeti",
        tipViewModel = tipViewModel,
        onBackClick = onBackClick
    )
}