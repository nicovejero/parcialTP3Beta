package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beta.data.model.PetModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdopcionViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val _petOptions = MutableLiveData<FirestoreRecyclerOptions<PetModel>>()
    val petOptions: LiveData<FirestoreRecyclerOptions<PetModel>> = _petOptions
    private val _adoptedPetsQuery = MutableLiveData<Query?>()
    val adoptedPetsQuery: LiveData<Query?> = _adoptedPetsQuery

    // LiveData for when there are no adopted pets
    private val _noAdoptionsAvailable = MutableLiveData<Boolean>()
    val noAdoptionsAvailable: LiveData<Boolean> = _noAdoptionsAvailable

    fun getUserId() = auth.currentUser?.uid ?: ""

    fun getInitialPetQuery(): Query {
        return db.collection("pets")
            .whereEqualTo("petAdopted", false)
            .whereEqualTo("petOwner", false)
    }
}
