package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beta.data.model.PetModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _petBreeds = MutableLiveData<List<String>>()
    val petBreeds: LiveData<List<String>> = _petBreeds
    private val _petOptions = MutableLiveData<FirestoreRecyclerOptions<PetModel>>()
    val petOptions: LiveData<FirestoreRecyclerOptions<PetModel>> = _petOptions
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchPetBreeds() {
        db.collection("pets")
            .whereEqualTo("petAdopted", false)
            .get()
            .addOnSuccessListener { result ->
                val breeds = result.documents.mapNotNull { it.getString("petBreed") }.distinct()
                _petBreeds.value = breeds
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.localizedMessage
            }
    }

    fun getUserId() = auth.currentUser?.uid ?: ""

    fun getInitialPetQuery(): Query {
        return db.collection("pets")
            .whereEqualTo("petAdopted", false)
    }

    fun updateSelectedBreeds(selectedBreeds: Set<String>) {
        val query: Query = db.collection("pets").whereEqualTo("petAdopted", false)
            .also {
                if (selectedBreeds.isNotEmpty()) {
                    it.whereIn("petBreed", selectedBreeds.toList())
                }
            }

        val options = FirestoreRecyclerOptions.Builder<PetModel>()
            .setQuery(query, PetModel::class.java)
            .build()
        _petOptions.value = options
    }
}
