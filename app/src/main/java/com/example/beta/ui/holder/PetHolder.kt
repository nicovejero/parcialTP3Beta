package com.example.beta.ui.holder

import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.ItemFragmentMascotaBinding

class PetHolder(private val binding: ItemFragmentMascotaBinding) : RecyclerView.ViewHolder(binding.root) {

    val toggleButton: ToggleButton = binding.toggleBookmark

    fun bind(petModel: PetModel) {
        binding.tvCardNombre.text = petModel.petName
        binding.tvCardEdad.text = petModel.petAge.toString()
        binding.tvCardGenero.text = petModel.petGender
        binding.tvCardRaza.text = petModel.petBreed
        binding.tvCardSubRaza.text = petModel.petSubBreed ?: ""

        Glide.with(itemView)
            .load(petModel.urlImage[0])
            .into(binding.ivCardBackGround)
    }

    fun getCardLayout() = binding.cardAdoption
}
