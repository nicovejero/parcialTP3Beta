package com.example.beta.ui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beta.data.model.PetModel
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
        db.collection("pets").document(petId).get()
            .addOnSuccessListener { documentSnapshot ->
                val petModel = documentSnapshot.toObject(PetModel::class.java)
                val urls = petModel?.urlImage ?: arrayListOf()
                _imageUrls.postValue(urls.toCollection(ArrayList()))
            }
            .addOnFailureListener { e ->
                _imageUrls.postValue(arrayListOf())
            }
    }

    fun adoptPet(petModel: PetModel, userId: String) {
        updateUserAdoptedPets(userId, petModel.petId) { exception ->
            if (exception == null) {
                markPetAsAdopted(petModel.petId)
            } else {
                _operationStatus.postValue(OperationStatus.Failure(exception.message ?: "Unknown error"))
            }
        }
        removePetFromFavorites(userId, petModel.petId)
    }
    private fun updateUserAdoptedPets(userId: String, petId: String, onComplete: (Exception?) -> Unit) {
        val adoptedPetsRef = db.collection("users").document(userId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(adoptedPetsRef)
            val adoptedPets = snapshot.get("adopted") as? MutableList<String> ?: mutableListOf()
            adoptedPets.add(petId)
            transaction.set(adoptedPetsRef, mapOf("adopted" to adoptedPets))
        }
            .addOnSuccessListener {
                onComplete(null)
            }
            .addOnFailureListener { exception ->
                onComplete(exception)
            }
    }

    fun markPetAsAdopted(petId: String) {
        db.collection("pets").document(petId)
            .update("petAdopted", true)
            .addOnSuccessListener {
                _operationStatus.postValue(OperationStatus.Success)
            }
            .addOnFailureListener { e ->
                _operationStatus.postValue(OperationStatus.Failure(e.message ?: "Unknown error"))
            }
    }

    private suspend fun markPetAsAdoptedInDatabase(petId: String) {
        withContext(Dispatchers.IO) {
            db.collection("pets").document(petId)
                .update("petAdopted", true)
                .await()
        }
    }

    private fun removePetFromFavorites(userId: String, petId: String) {
        db.collection("users").document(userId)
            .collection("bookmarks")
            .document(petId)
            .delete()
            .addOnSuccessListener {
                // If you need to do something on success, you can do it here
            }
            .addOnFailureListener { e ->
                // If you need to handle the error, you can do it here
            }
    }

    sealed class OperationStatus {
        object Success : OperationStatus()
        class Failure(val message: String) : OperationStatus()
    }
}
