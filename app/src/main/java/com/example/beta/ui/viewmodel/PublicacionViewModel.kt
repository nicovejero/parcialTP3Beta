package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beta.data.database.entities.Pet
import com.example.beta.domain.GetBreeds
import com.example.beta.domain.GetSubBreeds
import com.example.beta.domain.model.Breed
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.beta.util.Result
import javax.inject.Inject

@HiltViewModel
class PublicacionViewModel @Inject constructor(
    private val getBreeds: GetBreeds,
    private val getSubBreeds: GetSubBreeds // Renamed for clarity
) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val breedsLiveData = MutableLiveData<List<Breed>>()
    val isLoading = MutableLiveData<Boolean>()

    private val _subBreedsLiveData = MutableLiveData<List<String>>()
    val subBreedsLiveData: LiveData<List<String>> = _subBreedsLiveData

    fun onCreate() {
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                val breedsList = getBreeds()
                breedsLiveData.postValue(breedsList)
            } catch (e: Exception) {
                breedsLiveData.postValue(emptyList())
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun onBreedSelected(breedName: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                val subBreedsList = getSubBreeds(breedName).map { it.subBreedName }
                _subBreedsLiveData.postValue(subBreedsList)
            } catch (e: Exception) {
                _subBreedsLiveData.postValue(emptyList())
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
