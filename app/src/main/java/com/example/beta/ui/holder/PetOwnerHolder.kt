package com.example.beta.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.ItemFragmentMascotaOwnerBinding

class PetOwnerHolder(private val binding: ItemFragmentMascotaOwnerBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(petModel: PetModel) {
        binding.tvCardAdoptedNombre.text = petModel.petName
        binding.tvCardAdoptedEdad.text = petModel.petAge.toString()
        binding.tvCardAdoptedGenero.text = petModel.petGender
        binding.tvCardAdoptedRaza.text = petModel.petBreed
        binding.tvCardAdoptedSubRaza.text = petModel.petSubBreed ?: ""

        Glide.with(itemView)
            .load(petModel.urlImage[0])
            .into(binding.ivCardAdoptedBackGround)
    }

    fun getCardLayout() = binding.cardOwnerAdopted
}
