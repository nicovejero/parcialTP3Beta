package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.beta.data.BreedRepository
import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.util.Result
import com.example.beta.data.database.entities.Pet
import com.example.beta.domain.GetBreeds
import com.example.beta.domain.model.Breed
import com.example.beta.domain.model.SubBreed
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicacionViewModel @Inject constructor(
    private val getBreeds: GetBreeds,
    //private val getSubBreeds: GetSubBreeds
) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    val breedsLiveData = MutableLiveData<List<Breed>>()
    val subBreedModel = MutableLiveData<SubBreed>()
    val isLoading = MutableLiveData<Boolean>()

    fun onCreate() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getBreeds()
            if (result.isNotEmpty()) {
                breedsLiveData.postValue(result) // Post the entire list to LiveData
                isLoading.postValue(false)
            }
        }
    }

    fun addPet(pet: Pet, onComplete: (Result<String>) -> Unit) {
        db.collection("pets")
            .add(pet.toMap()) // Corrected: Ensure there is a toMap() method in Pet class
            .addOnSuccessListener { documentReference ->
                onComplete(Result.Success(documentReference.id))
            }
            .addOnFailureListener { e ->
                onComplete(Result.Error(e))
            }
    }
}