package com.example.tactimind.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tactimind.model.Tip
import com.example.tactimind.viewmodel.TipViewModel

@Composable
fun TipScreen(
    game: String,
    screenTitle: String,
    tipViewModel: TipViewModel,
    onBackClick: () -> Unit
) {
    val categories = remember(game) {
        if (game.equals("TFT", ignoreCase = true)) {
            listOf("Ekonomija", "Compovi", "Itemi")
        } else {
            listOf("Laning", "Buildovi", "Općenito")
        }
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var editingTip by remember { mutableStateOf<Tip?>(null) }
    var deletingTip by remember { mutableStateOf<Tip?>(null) }
    var randomTip by remember { mutableStateOf<Tip?>(null) }
    var selectedCategory by remember(game) { mutableStateOf("Sve") }

    val filteredTips =
        if (selectedCategory == "Sve") {
            tipViewModel.tips
        } else {
            tipViewModel.tips.filter { tip ->
                tip.category == selectedCategory
            }
        }

    LaunchedEffect(game) {
        tipViewModel.loadTips(game)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBackClick) {
                Text("← Natrag")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    tipViewModel.clearError()
                    showAddDialog = true
                },
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("+ Dodaj savjet")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = screenTitle,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Prikazano: ${filteredTips.size} od ${tipViewModel.tips.size}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.height(10.dp))

        CategoryChips(
            categories = listOf("Sve") + categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                selectedCategory = category
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = {
                randomTip = filteredTips.random()
            },
            enabled = filteredTips.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.75f)
            )
        ) {
            Text("✦ Prikaži nasumični savjet")
        }

        Spacer(modifier = Modifier.height(14.dp))

        tipViewModel.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (tipViewModel.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (tipViewModel.tips.isEmpty()) {
            Text("Još nema savjeta.")
        } else if (filteredTips.isEmpty()) {
            Text("Nema savjeta u ovoj kategoriji.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredTips,
                    key = { tip -> tip.id }
                ) { tip ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.45f
                            )
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(
                                alpha = 0.90f
                            )
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Text(
                                text = tip.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = tip.category.ifBlank {
                                    "Bez kategorije"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = tip.description,
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.90f
                                )
                            )

                            if (tipViewModel.canEditTip(tip)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            tipViewModel.clearError()
                                            editingTip = tip
                                        }
                                    ) {
                                        Text("Uredi")
                                    }

                                    TextButton(
                                        onClick = {
                                            tipViewModel.clearError()
                                            deletingTip = tip
                                        }
                                    ) {
                                        Text(
                                            text = "Obriši",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        TipDialog(
            dialogTitle = "Dodaj savjet",
            categories = categories,
            initialTitle = "",
            initialDescription = "",
            initialCategory = categories.first(),
            isLoading = tipViewModel.isLoading,
            errorMessage = tipViewModel.errorMessage,
            onConfirm = { title, description, category ->
                tipViewModel.addTip(
                    title = title,
                    description = description,
                    game = game,
                    category = category
                ) {
                    showAddDialog = false
                }
            },
            onDismiss = {
                tipViewModel.clearError()
                showAddDialog = false
            }
        )
    }

    editingTip?.let { tip ->
        TipDialog(
            dialogTitle = "Uredi savjet",
            categories = categories,
            initialTitle = tip.title,
            initialDescription = tip.description,
            initialCategory = tip.category,
            isLoading = tipViewModel.isLoading,
            errorMessage = tipViewModel.errorMessage,
            onConfirm = { title, description, category ->
                tipViewModel.updateTip(
                    tip = tip,
                    title = title,
                    description = description,
                    category = category
                ) {
                    editingTip = null
                }
            },
            onDismiss = {
                tipViewModel.clearError()
                editingTip = null
            }
        )
    }

    deletingTip?.let { tip ->
        AlertDialog(
            onDismissRequest = {
                deletingTip = null
            },
            title = {
                Text("Obrisati savjet?")
            },
            text = {
                Text("Jesi li siguran da želiš obrisati „${tip.title}”?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        tipViewModel.deleteTip(tip)
                        deletingTip = null
                    }
                ) {
                    Text(
                        text = "Obriši",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deletingTip = null
                    }
                ) {
                    Text("Odustani")
                }
            }
        )
    }

    randomTip?.let { tip ->
        AlertDialog(
            onDismissRequest = {
                randomTip = null
            },
            title = {
                Text(tip.title)
            },
            text = {
                Column {
                    Text(
                        text = tip.category.ifBlank {
                            "Bez kategorije"
                        },
                        color = MaterialTheme.colorScheme.tertiary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(tip.description)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        randomTip = null
                    }
                ) {
                    Text("U redu")
                }
            }
        )
    }
}

@Composable
private fun TipDialog(
    dialogTitle: String,
    categories: List<String>,
    initialTitle: String,
    initialDescription: String,
    initialCategory: String,
    isLoading: Boolean,
    errorMessage: String?,
    onConfirm: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember(initialTitle) {
        mutableStateOf(initialTitle)
    }

    var description by remember(initialDescription) {
        mutableStateOf(initialDescription)
    }

    var category by remember(initialCategory) {
        mutableStateOf(
            initialCategory.ifBlank {
                categories.first()
            }
        )
    }

    AlertDialog(
        onDismissRequest = {
            if (!isLoading) {
                onDismiss()
            }
        },
        title = {
            Text(dialogTitle)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text("Naslov")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("Opis")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Kategorija",
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(6.dp))

                CategoryChips(
                    categories = categories,
                    selectedCategory = category,
                    onCategorySelected = { selected ->
                        category = selected
                    }
                )

                errorMessage?.let { message ->
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(title, description, category)
                },
                enabled = !isLoading
            ) {
                Text(
                    if (isLoading) {
                        "Spremanje..."
                    } else {
                        "Spremi"
                    }
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Odustani")
            }
        }
    )
}

@Composable
private fun CategoryChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = {
                    onCategorySelected(category)
                },
                label = {
                    Text(category)
                }
            )
        }
    }
}