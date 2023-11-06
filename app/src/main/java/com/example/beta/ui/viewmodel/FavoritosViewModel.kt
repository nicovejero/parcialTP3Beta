package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beta.data.model.PetModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class FavoritosViewModel : ViewModel() {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val db = FirebaseFirestore.getInstance()

    // LiveData for the pets that are bookmarked
    private val _bookmarkedPets = MutableLiveData<List<PetModel>?>()

    // LiveData for the IDs of the pets that are bookmarked
    private val _userBookmarks = MutableLiveData<List<String>?>()
    val userBookmarks: LiveData<List<String>?> = _userBookmarks

    val noBookmarksAvailable = MutableLiveData<Boolean>()

    init {
        fetchBookmarkedPets(userId) // Fetch bookmarks when ViewModel is created
    }

    fun fetchBookmarkedPets(userId: String) {
        val userRef = db.collection("users").document(userId)
        userRef.get().addOnSuccessListener { document ->
            val bookmarkedIds = document["bookmarks"] as? List<String> ?: listOf()
            _userBookmarks.value = bookmarkedIds // Update the LiveData with the list of bookmarked IDs

            if (bookmarkedIds.isNotEmpty()) {
                fetchPets(bookmarkedIds)
            } else {
                noBookmarksAvailable.value = true
            }
        }.addOnFailureListener { e ->
            noBookmarksAvailable.value = true
        }
    }

    fun fetchPets(bookmarkedIds: List<String>) {
        db.collection("pets").whereIn(FieldPath.documentId(), bookmarkedIds)
            .get()
            .addOnSuccessListener { documents ->
                val petsList = documents.toObjects(PetModel::class.java)
                if (petsList.isNullOrEmpty()) {
                    noBookmarksAvailable.value = true
                } else {
                    _bookmarkedPets.value = petsList
                    noBookmarksAvailable.value = false
                }
            }
            .addOnFailureListener {
                noBookmarksAvailable.value = true
            }
    }
}
