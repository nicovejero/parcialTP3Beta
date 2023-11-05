package com.example.beta.ui.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.bumptech.glide.Glide
import com.example.beta.databinding.ItemFragmentMascotaBinding

class PetHolder (v: View) : RecyclerView.ViewHolder(v) {
        private lateinit var binding: ItemFragmentMascotaBinding
        private var view: View

        init {
            this.view = v
        }

        fun setCard(petName: String, petAge: Int, petGender: Boolean, petImg: String, petBreed: String, petSubBreed: String?) {
            binding = ItemFragmentMascotaBinding.bind(view)
            val cardNombre: TextView = binding.tvCardNombre
            val cardEdad: TextView = binding.tvCardEdad
            val cardGenero: TextView = binding.tvCardGenero
            val cardRaza: TextView = binding.tvCardRaza
            val cardSubRaza: TextView = binding.tvCardSubRaza
            val cardImg: ImageView =binding.ivCardBackGround

            cardNombre.text = petName
            cardEdad.text = petAge.toString()
            cardGenero.text = if (petGender) "Macho" else "Hembra"
            cardRaza.text = petBreed
            cardSubRaza.text = petSubBreed
            Glide.with(view)
                .load(petImg)
                .into(cardImg)

        }

        fun getCardLayout(): CardView {
            return binding.cardAdoption
        }
}