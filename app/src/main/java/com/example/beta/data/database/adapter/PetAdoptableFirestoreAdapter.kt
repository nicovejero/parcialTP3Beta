package com.example.beta.data.database.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.ItemFragmentMascotaBinding
import com.example.beta.ui.holder.PetHolder
import com.example.beta.ui.view.HomeFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PetAdoptableFirestoreAdapter(
    options: FirestoreRecyclerOptions<PetModel>,
    private val userId: String,
    private val lifecycle: Lifecycle
) :
    FirestoreRecyclerAdapter<PetModel, PetHolder>(options), DefaultLifecycleObserver {

    init {
        lifecycle.addObserver(this) // Add as an observer to lifecycle events
    }

    private var userBookmarks = mutableListOf<String>()

    init {
        fetchUserBookmarks()
    }
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Start listening to updates from Firestore
        startListening()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // Stop listening to updates from Firestore
        stopListening()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        // Clean up resources if needed
        lifecycle.removeObserver(this)
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

    override fun onBindViewHolder(holder: PetHolder, position: Int, model: PetModel) {
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
