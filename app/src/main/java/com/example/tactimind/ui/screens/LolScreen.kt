package com.example.tactimind.ui.screens

import androidx.compose.runtime.Composable
import com.example.tactimind.viewmodel.TipViewModel

@Composable
fun LolScreen(
    tipViewModel: TipViewModel,
    onBackClick: () -> Unit
) {
    TipScreen(
        game = "LOL",
        screenTitle = "League of Legends savjeti",
        tipViewModel = tipViewModel,
        onBackClick = onBackClick
    )
}