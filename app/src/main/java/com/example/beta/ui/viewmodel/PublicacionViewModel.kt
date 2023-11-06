package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beta.data.model.PetModel
import com.example.beta.domain.GetBreeds
import com.example.beta.domain.GetSubBreeds
import com.example.beta.domain.model.Breed
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

@HiltViewModel
class PublicacionViewModel @Inject constructor(
    private val getBreeds: GetBreeds,
    private val getSubBreeds: GetSubBreeds // Renamed for clarity
) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    val breedsLiveData = MutableLiveData<List<Breed>>()
    val isLoading = MutableLiveData<Boolean>()
    private val _resetFields = MutableLiveData<Boolean?>().apply { value = false } // default to false
    val resetFields: LiveData<Boolean?> = _resetFields

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

    fun addPet(petModel: PetModel) {
        if (userId != null) {
            isLoading.postValue(true)
            db.collection("pets")
                .add(petModel.toMap()) // Ensure Pet has a toMap() method
                .addOnSuccessListener { documentReference ->
                    val petId = documentReference.id
                    petModel.petOwner = userId
                    petModel.petId = petId
                    db.collection("pets").document(petId)
                        .set(petModel)
                        .addOnSuccessListener {
                            isLoading.postValue(false)
                            _resetFields.postValue(true) // Signal that fields should be reset
                        }
                        .addOnFailureListener { e ->
                            isLoading.postValue(false)
                            _resetFields.postValue(false)
                            // Handle failure
                        }
                }
                .addOnFailureListener { e ->
                    isLoading.postValue(false)
                    // Handle failure
                }
        }
    }

    fun onFieldsResetComplete() {
        _resetFields.value = false
    }
}
