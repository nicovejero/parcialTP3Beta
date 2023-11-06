package com.example.beta.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.beta.data.model.BreedModel
import com.example.beta.data.model.PetModel
import com.example.beta.domain.GetBreeds
import com.example.beta.domain.GetSubBreeds
import com.example.beta.domain.model.Breed
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PublicacionViewModel @Inject constructor(
    private val getBreeds: GetBreeds,
    private val getSubBreeds: GetSubBreeds // Renamed for clarity
) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    //val breedsLiveData = MutableLiveData<List<Breed>>()
    val isLoading = MutableLiveData<Boolean>()
    private val _resetFields = MutableLiveData<Boolean?>().apply { value = false }
    val resetFields: LiveData<Boolean?> = _resetFields

    private val _breedsLiveData = MutableLiveData<List<Breed>>()
    val breedsLiveData: LiveData<List<Breed>> = _breedsLiveData

    private val _subBreedsLiveData = MutableLiveData<List<String>>()
    val subBreedsLiveData: LiveData<List<String>> = _subBreedsLiveData

    private val _breeds = MutableStateFlow<List<Breed>>(emptyList())
    val breeds: StateFlow<List<Breed>> = _breeds

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadBreeds() {
        viewModelScope.launch {
            try {
                // Use the GetBreeds use-case to load breeds
                val breedsResult = getBreeds.invoke()
                _breeds.value = breedsResult
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            }
        }
    }

    fun onCreate() {
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                // Directly assign the result from getBreeds to breedsLiveData
                val breedsList = getBreeds()
                _breedsLiveData.postValue(breedsList)
            } catch (e: Exception) {
                _breedsLiveData.postValue(emptyList())
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun onBreedSelected(breedId: Int) {
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                // Assuming getSubBreeds returns List<SubBreed>, map it to List<String>
                val subBreedsList = getSubBreeds(breedId).map { it.subBreedName }
                _subBreedsLiveData.postValue(subBreedsList)
            } catch (e: Exception) {
                _subBreedsLiveData.postValue(emptyList())
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun addPet(petModel: PetModel) {
        if (userId != null) {
            isLoading.postValue(true)

            // Set the creation timestamp here before adding to database
            petModel.creationTimestamp = System.currentTimeMillis()

            db.collection("pets")
                .add(petModel.toMap()) // Ensure Pet has a toMap() method
                .addOnSuccessListener { documentReference ->
                    val petId = documentReference.id
                    Log.d("Firestore", "Document added with ID: $petId")

                    petModel.petOwner = userId
                    petModel.petId = petId
                    Log.d("Firestore", "petModel updated with petId: ${petModel.petId}")

                    db.collection("pets").document(petId)
                        .set(petModel.toMap())
                        .addOnSuccessListener {
                            Log.d("Firestore", "Document with ID: $petId updated successfully")
                            // Further success handling
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error updating document", e)
                            // Handle failure
                        }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding document", e)
                    // Handle failure
                }
        }
    }

    fun onFieldsResetComplete() {
        _resetFields.value = false
    }
}
