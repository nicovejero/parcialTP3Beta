package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.beta.data.model.PetModel
import com.example.beta.ui.view.HomeFragmentDirections
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PetInAdoptionDetailViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _operationStatus = MutableLiveData<OperationStatus>()
    val operationStatus: LiveData<OperationStatus>
        get() = _operationStatus

    private val _imageUrls = MutableLiveData<ArrayList<String>>() // Changed to ArrayList
    val imageUrls: LiveData<ArrayList<String>> = _imageUrls // Changed to ArrayList

    fun getImageUrlsForPet(petId: String) {
        viewModelScope.launch {
            try {
                val petDocSnapshot = withContext(Dispatchers.IO) {
                    db.collection("pets").document(petId).get().await()
                }
                val petModel = petDocSnapshot.toObject(PetModel::class.java)
                val urls = ArrayList<String>() // Changed to ArrayList
                // Check if urlImage is a String or a List and handle accordingly
                when (val urlImage = petModel?.urlImage) {
                    is List<*> -> {
                        // If it's a list, add all items that are strings
                        urls.addAll(urlImage.filterIsInstance<String>())
                    }
                    // Add any other cases if necessary
                }

                _imageUrls.postValue(urls) // Post the ArrayList to the LiveData
            } catch (e: Exception) {
                _imageUrls.postValue(arrayListOf()) // On error, post an empty ArrayList
            }
        }
    }


    fun adoptPet(petModel: PetModel, userId: String) {
        viewModelScope.launch {
            try {
                updateUserAdoptedPets(userId, petModel.petId)
                markPetAsAdopted(petModel.petId)
                removePetFromFavorites(userId, petModel.petId)
                _operationStatus.postValue(OperationStatus.Success)
            } catch (e: Exception) {
                _operationStatus.postValue(OperationStatus.Failure(e.message ?: "Unknown error"))
            }
        }
    }

    private suspend fun updateUserAdoptedPets(userId: String, petId: String) {
        withContext(Dispatchers.IO) {
            // Fetch the user's adopted pets list, add the new pet, and update
            // Note that this logic is simplified; handle the transaction logic as necessary.
            val adoptedPetsRef = db.collection("users").document(userId)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(adoptedPetsRef)
                val adoptedPets = snapshot.get("adopted") as? MutableList<String> ?: mutableListOf()
                adoptedPets.add(petId)
                transaction.update(adoptedPetsRef, "adopted", adoptedPets)
            }.await()
        }
    }

    fun markPetAsAdopted(petId: String) {
        viewModelScope.launch {
            try {
                // Call your previously defined private suspend function
                markPetAsAdoptedInDatabase(petId)
                // Update operation status upon successful completion
                _operationStatus.postValue(OperationStatus.Success)
            } catch (e: Exception) {
                // Post an error status with the exception message
                _operationStatus.postValue(OperationStatus.Failure(e.message ?: "Unknown error"))
            }
        }
    }

    private suspend fun markPetAsAdoptedInDatabase(petId: String) {
        withContext(Dispatchers.IO) {
            db.collection("pets").document(petId)
                .update("petAdopted", true)
                .await()
        }
    }

    private suspend fun removePetFromFavorites(userId: String, petId: String) {
        withContext(Dispatchers.IO) {
            db.collection("users").document(userId)
                .collection("bookmarks")
                .document(petId)
                .delete()
                .await()
        }
    }

    sealed class OperationStatus {
        object Success : OperationStatus()
        class Failure(val message: String) : OperationStatus()
    }
}
