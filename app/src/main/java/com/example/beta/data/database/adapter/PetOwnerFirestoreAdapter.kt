package com.example.beta.data.database.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.ItemFragmentMascotaOwnerBinding
import com.example.beta.ui.holder.PetHolder
import com.example.beta.ui.holder.PetOwnerHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class PetOwnerFirestoreAdapter(
    options: FirestoreRecyclerOptions<PetModel>
) : FirestoreRecyclerAdapter<PetModel, PetOwnerHolder>(options) {

    override fun onBindViewHolder(holder: PetOwnerHolder, position: Int, model: PetModel) {
        holder.bind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetOwnerHolder {
        val binding = ItemFragmentMascotaOwnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetOwnerHolder(binding)
    }
}
