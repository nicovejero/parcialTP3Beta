package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdopcionViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    // LiveData for Firestore query
    private val _adoptedPetsQuery = MutableLiveData<Query?>()
    val adoptedPetsQuery: LiveData<Query?> = _adoptedPetsQuery

    // LiveData for when there are no adopted pets
    private val _noAdoptionsAvailable = MutableLiveData<Boolean>()
    val noAdoptionsAvailable: LiveData<Boolean> = _noAdoptionsAvailable

    fun fetchAdoptedPets() {
        userId?.let { userId ->
            val userDocRef = db.collection("users").document(userId)
            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                val adoptedPetsList =
                    documentSnapshot.data?.get("adoptedPets") as? List<String> ?: emptyList()

                if (adoptedPetsList.isEmpty()) {
                    _noAdoptionsAvailable.postValue(true)
                } else {
                    // Create a query for the adopted pets
                    // Note: This assumes you have a structure where pet details can be directly fetched by their IDs
                    val petsQuery =
                        db.collection("pets").whereIn(FieldPath.documentId(), adoptedPetsList)
                    _adoptedPetsQuery.postValue(petsQuery)
                    _noAdoptionsAvailable.postValue(false)
                }
            }.addOnFailureListener {
                // Handle any errors here
                _noAdoptionsAvailable.postValue(true)
            }
        }

    }
}
