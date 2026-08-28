package com.example.tactimind.model

import com.google.firebase.Timestamp

data class Tip(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val game: String = "",
    val authorId: String = "",
    val createdAt: Timestamp = Timestamp.now()
)