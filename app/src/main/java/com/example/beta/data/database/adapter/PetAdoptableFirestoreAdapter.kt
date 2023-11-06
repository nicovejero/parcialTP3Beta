package com.example.beta.data.database.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.ItemFragmentMascotaBinding
import com.example.beta.ui.holder.PetHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class PetAdoptableFirestoreAdapter(
    options: FirestoreRecyclerOptions<PetModel>,
    private val userId: String
) : FirestoreRecyclerAdapter<PetModel, PetHolder>(options) {

    private var userBookmarks = mutableListOf<String>()

    fun updateUserBookmarks(bookmarks: List<String>) {
        userBookmarks.clear()
        userBookmarks.addAll(bookmarks)
        notifyDataSetChanged() // Refresh the RecyclerView with the new bookmarks
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int, model: PetModel) {
        holder.bind(model)
        // Set the bookmark toggle button state
        val petId = snapshots.getSnapshot(position).id
        holder.toggleButton.isChecked = userBookmarks.contains(petId)

        // Set click listener for the entire card
        holder.getCardLayout().setOnClickListener {
            // Navigation logic
        }

        // Set click listener for the bookmark toggle button
        holder.toggleButton.setOnClickListener {
            val isBookmarked = holder.toggleButton.isChecked
            val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)
            if (isBookmarked) {
                userRef.update("bookmarks", FieldValue.arrayUnion(petId))
            } else {
                userRef.update("bookmarks", FieldValue.arrayRemove(petId))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder {
        val binding = ItemFragmentMascotaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetHolder(binding)
    }

    // You may no longer need onStart, onStop, onDestroy if you manage listening in the Fragment
}
