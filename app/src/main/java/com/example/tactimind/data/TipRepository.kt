package com.example.tactimind.data

import com.example.tactimind.model.Tip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TipRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val tipsCollection = firestore.collection("tips")

    fun addTip(
        title: String,
        description: String,
        game: String,
        category: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onResult(false, "Korisnik nije prijavljen.")
            return
        }

        val document = tipsCollection.document()

        val tip = Tip(
            id = document.id,
            title = title,
            description = description,
            game = game,
            category = category,
            authorId = userId
        )

        document.set(tip)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                onResult(
                    false,
                    exception.message ?: "Dodavanje savjeta nije uspjelo."
                )
            }
    }

    fun getTips(
        game: String,
        onResult: (List<Tip>, String?) -> Unit
    ) {
        tipsCollection
            .whereEqualTo("game", game)
            .get()
            .addOnSuccessListener { result ->
                val tips = result.documents
                    .mapNotNull { document ->
                        document.toObject(Tip::class.java)
                            ?.copy(id = document.id)
                    }
                    .sortedByDescending { tip ->
                        tip.createdAt
                    }

                onResult(tips, null)
            }
            .addOnFailureListener { exception ->
                onResult(
                    emptyList(),
                    exception.message ?: "Dohvaćanje savjeta nije uspjelo."
                )
            }
    }

    fun updateTip(
        tipId: String,
        title: String,
        description: String,
        category: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        tipsCollection.document(tipId)
            .update(
                mapOf(
                    "title" to title,
                    "category" to category,
                    "description" to description
                )
            )
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                onResult(
                    false,
                    exception.message ?: "Uređivanje savjeta nije uspjelo."
                )
            }
    }

    fun deleteTip(
        tipId: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        tipsCollection.document(tipId)
            .delete()
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                onResult(
                    false,
                    exception.message ?: "Brisanje savjeta nije uspjelo."
                )
            }
    }

    fun currentUserId(): String? {
        return auth.currentUser?.uid
    }
}