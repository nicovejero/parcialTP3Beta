package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beta.data.model.ChipModel
import com.example.beta.data.model.PetModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _breeds = MutableLiveData<List<ChipModel>>()
    val breeds: LiveData<List<ChipModel>> = _breeds
    val petBreeds = MutableLiveData<List<String>>()
    val petOptions = MutableLiveData<FirestoreRecyclerOptions<PetModel>>()
    val errorMessage = MutableLiveData<String>()

    private val _petQuery = MutableLiveData<Query>()
    val petQuery: LiveData<Query> = _petQuery

    fun fetchPetBreeds() {
        db.collection("pets")
            .get()
            .addOnSuccessListener { result ->
                val breeds = result.documents.mapNotNull { it.getString("breed") }.distinct()
                petBreeds.value = breeds
            }
            .addOnFailureListener { e ->
                errorMessage.value = e.localizedMessage
            }
    }

    fun getInitialPetQuery(): Query {
        // Return a query that fetches non-adopted pets ordered by some criteria, such as timestamp
        return db.collection("pets")
            //.whereEqualTo("adopted", false)
            //.orderBy("creationtimestamp", Query.Direction.DESCENDING) // Replace "timestamp" with your actual field name
    }

    fun getOrderedPetQuery(): Query {
        return db.collection("pets")
        .whereEqualTo("adopted", false)
        .orderBy("creationtimestamp", Query.Direction.DESCENDING) // Replace "timestamp" with your actual field name
    }

    fun getUserId(): String {
        val userId: String? = auth.currentUser?.uid
        if (userId != null) {
            return userId
        }
        else {
            return ""
        }
    }

    fun updateSelectedBreeds(selectedBreeds: Set<String>) {
        val query: Query = if (selectedBreeds.isNotEmpty()) {
            // If breeds are selected, adjust the query to filter by those breeds
            db.collection("pets").whereIn("breed", selectedBreeds.toList()).whereEqualTo("adopted", false)
        } else {
            // If no breeds are selected, use the original query that fetches all pets that are not adopted
            db.collection("pets").whereEqualTo("adopted", false)
        }

        // Create new FirestoreRecyclerOptions for the updated query
        val options = FirestoreRecyclerOptions.Builder<PetModel>()
            .setQuery(query, PetModel::class.java)
            .build()

        // Post the new options to the LiveData so that the UI can update the adapter
        petOptions.value = options
    }
}
