package com.example.beta.data.database.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.data.database.entities.Pet
import com.example.beta.ui.holder.PetHolder
import com.example.beta.ui.view.HomeFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class PetAdoptableFirestoreAdapter(options: FirestoreRecyclerOptions<Pet>) :
    FirestoreRecyclerAdapter<Pet, PetHolder>(options) {

    override fun onBindViewHolder(holder: PetHolder, position: Int, model: Pet) {

        holder.setCard(model.petName, model.petAge, model.petGender, model.urlImage, model.petBreed, model.petSubBreed)

        holder.getCardLayout().setOnClickListener {
            // Obtain the position of the holder
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedPet = getItem(position)
                Log.d("PetAdoptableFirestoreAdapter", "onBindViewHolder: " + clickedPet.petName)
                val action = HomeFragmentDirections.actionHomeFragmentToPetInAdoptionDetailFragment(clickedPet, "asd")
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fragment_mascota, parent, false)
        return PetHolder(view)
    }
}
