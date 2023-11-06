package com.example.beta.ui.holder

import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.ItemFragmentMascotaBinding

class PetHolder(private val binding: ItemFragmentMascotaBinding) : RecyclerView.ViewHolder(binding.root) {

    val toggleButton: ToggleButton = binding.toggleBookmark

    fun setCard(petName: String, petAge: Int, petGender: Boolean, petImg: List<String>, petBreed: String, petSubBreed: String?) {
        binding.tvCardNombre.text = petName
        binding.tvCardEdad.text = petAge.toString()
        binding.tvCardGenero.text = if (petGender) "Macho" else "Hembra"
        binding.tvCardRaza.text = petBreed
        binding.tvCardSubRaza.text = petSubBreed ?: "" // Assuming petSubBreed can be null and you want to set an empty string in that case

        Glide.with(itemView)
            .load(petImg)
            .into(binding.ivCardBackGround)
    }

    fun bind(petModel: PetModel) {
        // ... Your binding logic ...

        // Load image with Glide, set text fields, etc.
        // Example:
        binding.tvCardNombre.text = petModel.petName
        // and so on for the rest of the pet's properties
    }

    fun getCardLayout() = binding.cardAdoption
}
