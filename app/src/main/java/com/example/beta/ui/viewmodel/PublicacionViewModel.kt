package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beta.data.database.entities.Pet
import com.example.beta.data.model.BreedModel
import com.example.beta.domain.GetBreeds
import com.example.beta.domain.model.Breed
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.beta.util.Result
import javax.inject.Inject

@HiltViewModel
class PublicacionViewModel @Inject constructor(
    private val getBreeds: GetBreeds
) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    //val breedsLiveData: LiveData<List<BreedModel>> =
    val breedsLiveData = MutableLiveData<List<Breed>>()
    val isLoading = MutableLiveData<Boolean>()

    fun onCreate() {
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                val breedsList = getBreeds() // Assuming this is a suspend function returning a List<BreedEntity>
                breedsLiveData.postValue(breedsList)
            } catch (e: Exception) {
                // Handle the error case
                breedsLiveData.postValue(emptyList())
                // You can also post the error to a LiveData if you want to show the error message in UI
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun addPet(pet: Pet, onComplete: (Result<String>) -> Unit) {
        db.collection("pets")
            .add(pet.toMap()) // Make sure Pet has a method toMap() that converts it to a Map
            .addOnSuccessListener { documentReference ->
                onComplete(Result.Success(documentReference.id)) // Assuming Result.Success is a custom class you've defined
            }
            .addOnFailureListener { e ->
                onComplete(Result.Error(e)) // Assuming Result.Error is a custom class you've defined
            }
    }
}
