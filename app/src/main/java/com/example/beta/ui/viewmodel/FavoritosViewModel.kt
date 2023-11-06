package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beta.data.database.entities.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FavoritosViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val _noBookmarksAvailable = MutableLiveData<Boolean>()
    val noBookmarksAvailable: LiveData<Boolean> = _noBookmarksAvailable


    private val _bookmarkedPetsQuery = MutableLiveData<Query>()
    val bookmarkedPetsQuery: LiveData<Query> = _bookmarkedPetsQuery

    fun fetchBookmarkedPets() {
        userId?.let { userId ->
            val userRef = db.collection("users").document(userId)
            userRef.get().addOnSuccessListener { document ->
                val bookmarks = document["bookmarks"] as? List<String> ?: emptyList()
                if (bookmarks.isNotEmpty()) {
                    val petsQuery = db.collection("pets").whereIn(FieldPath.documentId(), bookmarks)
                    _bookmarkedPetsQuery.value = petsQuery
                    _noBookmarksAvailable.value = false  // Inform that bookmarks are available
                } else {
                    // Handle the case where there are no bookmarks.
                    _noBookmarksAvailable.value = true   // Inform that there are no bookmarks
                }
            }.addOnFailureListener {
                // Handle any failures here.
                _noBookmarksAvailable.value = true       // You could use another LiveData to inform about the error.
            }
        } ?: run {
            // Handle the case where userId is null.
            _noBookmarksAvailable.value = true           // Inform that there are no bookmarks or error occurred.
        }
    }



    private fun handleNoBookmarks() {
        // Update the LiveData to inform the UI that there are no bookmarks.
        _noBookmarksAvailable.value = true
    }


    fun subscribeToBookmarkedPets() {
        userId?.let { userId ->
            val userRef = db.collection("users").document(userId)
            userRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle the error
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val bookmarks = snapshot["bookmarks"] as? List<String> ?: emptyList()
                    val petsQuery = db.collection("pets").whereIn(FieldPath.documentId(), bookmarks)
                    _bookmarkedPetsQuery.value = petsQuery
                }
            }
        }
    }
}
