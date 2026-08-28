package com.example.tactimind.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTip by remember { mutableStateOf<Tip?>(null) }
    var deletingTip by remember { mutableStateOf<Tip?>(null) }

    LaunchedEffect(game) {
        tipViewModel.loadTips(game)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onBackClick) {
                Text("Natrag")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    tipViewModel.clearError()
                    showAddDialog = true
                }
            ) {
                Text("Dodaj savjet")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = screenTitle,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        tipViewModel.errorMessage?.let {
            Text(
                text = it,
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
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = tipViewModel.tips,
                    key = { tip -> tip.id }
                ) { tip ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = tip.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = tip.description)

                            if (tipViewModel.canEditTip(tip)) {
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
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
                                        Text("Obriši")
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
            initialTitle = "",
            initialDescription = "",
            isLoading = tipViewModel.isLoading,
            errorMessage = tipViewModel.errorMessage,
            onConfirm = { title, description ->
                tipViewModel.addTip(
                    title = title,
                    description = description,
                    game = game
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
            initialTitle = tip.title,
            initialDescription = tip.description,
            isLoading = tipViewModel.isLoading,
            errorMessage = tipViewModel.errorMessage,
            onConfirm = { title, description ->
                tipViewModel.updateTip(
                    tip = tip,
                    title = title,
                    description = description
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
                    Text("Obriši")
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
}

@Composable
private fun TipDialog(
    dialogTitle: String,
    initialTitle: String,
    initialDescription: String,
    isLoading: Boolean,
    errorMessage: String?,
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember(initialTitle) {
        mutableStateOf(initialTitle)
    }

    var description by remember(initialDescription) {
        mutableStateOf(initialDescription)
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
                    onValueChange = { title = it },
                    label = { Text("Naslov") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Opis") },
                    modifier = Modifier.fillMaxWidth()
                )

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(title, description)
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