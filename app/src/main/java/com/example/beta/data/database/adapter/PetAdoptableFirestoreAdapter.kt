package com.example.beta.data.database.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.data.database.entities.Pet
import com.example.beta.databinding.ItemFragmentMascotaBinding
import com.example.beta.ui.holder.PetHolder
import com.example.beta.ui.view.HomeFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PetAdoptableFirestoreAdapter(options: FirestoreRecyclerOptions<Pet>, private val userId: String) :
    FirestoreRecyclerAdapter<Pet, PetHolder>(options) {

    private var userBookmarks = mutableListOf<String>()

    init {
        fetchUserBookmarks()
    }

    private fun fetchUserBookmarks() {
        val userRef = Firebase.firestore.collection("users").document(userId)
        userRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val bookmarks = documentSnapshot["bookmarks"]
                if (bookmarks is List<*>) {
                    userBookmarks = bookmarks.filterIsInstance<String>().toMutableList()
                }
                notifyDataSetChanged() // This will refresh the RecyclerView with the new bookmark statuses
            }
        }.addOnFailureListener { e ->
            // Handle error
            e.printStackTrace()
        }
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int, model: Pet) {
        holder.bind(model)

        // Set the toggle button based on whether the petId is in the bookmarks list
        val petId = snapshots.getSnapshot(position).id // Assuming petId is the document ID in Firestore
        holder.toggleButton.isChecked = userBookmarks.contains(petId)

        holder.getCardLayout().setOnClickListener {
            // Obtain the position of the holder
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedPet = getItem(position)
                Log.d("PetAdoptableFirestoreAdapter", "onBindViewHolder: " + clickedPet.petName)
                val action = HomeFragmentDirections.actionHomeFragmentToPetInAdoptionDetailFragment(clickedPet, userId)
                it.findNavController().navigate(action)
            }
        }


        holder.toggleButton.setOnClickListener {
            val isBookmarked = holder.toggleButton.isChecked
            val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

            if (isBookmarked) {
                // If it wasn't bookmarked before, add to bookmarks in Firestore
                if (!userBookmarks.contains(petId)) {
                    userRef.update("bookmarks", FieldValue.arrayUnion(petId))
                }
            } else {
                // If it was bookmarked, remove from bookmarks in Firestore
                userRef.update("bookmarks", FieldValue.arrayRemove(petId))
            }
        }
    }


    private fun updateUserBookmarks() {
        val userRef = Firebase.firestore.collection("users").document(userId)
        userRef.update("bookmarks", userBookmarks)
            .addOnSuccessListener {
                // Successfully updated bookmarks
            }
            .addOnFailureListener { e ->
                // Handle error
                e.printStackTrace()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder {
        val binding = ItemFragmentMascotaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetHolder(binding)
    }

}
