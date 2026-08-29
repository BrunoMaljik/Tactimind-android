package com.example.tactimind.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tactimind.data.TipRepository
import com.example.tactimind.model.Tip

class TipViewModel(
    private val repository: TipRepository = TipRepository()
) : ViewModel() {

    var tips by mutableStateOf<List<Tip>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadTips(game: String) {
        isLoading = true
        errorMessage = null

        repository.getTips(game) { loadedTips, message ->
            isLoading = false
            tips = loadedTips
            errorMessage = message
        }
    }

    fun addTip(
        title: String,
        description: String,
        game: String,
        category: String,
        onSuccess: () -> Unit
    ) {
        errorMessage = null

        if (title.isBlank() || description.isBlank()) {
            errorMessage = "Naslov i opis moraju biti popunjeni."
            return
        }

        if (category.isBlank()) {
            errorMessage = "Odaberi kategoriju."
            return
        }

        isLoading = true

        repository.addTip(
            title = title.trim(),
            description = description.trim(),
            game = game,
            category = category
        ) { success, message ->
            isLoading = false

            if (success) {
                loadTips(game)
                onSuccess()
            } else {
                errorMessage = message
            }
        }
    }

    fun updateTip(
        tip: Tip,
        title: String,
        description: String,
        category: String,
        onSuccess: () -> Unit
    ) {
        errorMessage = null

        if (title.isBlank() || description.isBlank()) {
            errorMessage = "Naslov i opis moraju biti popunjeni."
            return
        }

        if (category.isBlank()) {
            errorMessage = "Odaberi kategoriju."
            return
        }

        isLoading = true

        repository.updateTip(
            tipId = tip.id,
            title = title.trim(),
            description = description.trim(),
            category = category
        ) { success, message ->
            isLoading = false

            if (success) {
                loadTips(tip.game)
                onSuccess()
            } else {
                errorMessage = message
            }
        }
    }

    fun deleteTip(tip: Tip) {
        isLoading = true
        errorMessage = null

        repository.deleteTip(tip.id) { success, message ->
            isLoading = false

            if (success) {
                loadTips(tip.game)
            } else {
                errorMessage = message
            }
        }
    }

    fun canEditTip(tip: Tip): Boolean {
        return tip.authorId == repository.currentUserId()
    }

    fun clearError() {
        errorMessage = null
    }
}