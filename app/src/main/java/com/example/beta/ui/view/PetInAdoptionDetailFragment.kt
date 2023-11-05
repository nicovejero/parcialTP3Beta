package com.example.beta.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.beta.R
import com.example.beta.data.database.entities.Pet
import com.example.beta.databinding.FragmentPetInAdoptionDetailBinding
import com.google.firebase.auth.FirebaseAuth

class PetInAdoptionDetailFragment : Fragment() {
    private val args: PetInAdoptionDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentPetInAdoptionDetailBinding
    private val pet: Pet by lazy { args.pet } // Use lazy initialization
    private val uid: String? by lazy { FirebaseAuth.getInstance().currentUser?.uid }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetInAdoptionDetailBinding.inflate(inflater, container, false)
        val petName: TextView = binding.petDetailName
        val petBreed: TextView = binding.petDetailBreed
        val petSubBreed: TextView = binding.petDetailSubBreed
        val petWeight: TextView = binding.petDetailWeight
        //val petPicture = binding.petDetailPicture
        val petGender: TextView = binding.petDetailGender
        val petAge: TextView = binding.petDetailAge
        val petOwnerName = binding.petDetailOwnerName
        val petLocation = binding.petDetailLocation
        //val petOwnerPicture = binding.petDetailOwnerPicture
        val callOwnerButton: Button = binding.petDetailCallOwnerButton
        val adoptarButton: Button = binding.petDetailAdoptButton

        petName.text = petName.toString()
        petBreed.text = petBreed.toString()
        petSubBreed.text = petSubBreed.toString()
        petAge.text = petAge.toString()
        petWeight.text = petWeight.toString()
        petOwnerName.text = petOwnerName.toString()
        petGender.text = petGender.toString()
        petLocation.text = petLocation.toString()


        callOwnerButton.setOnClickListener {
            Toast.makeText(requireContext(), "Llamando al dueño!", Toast.LENGTH_SHORT).show()
        }

        adoptarButton.setOnClickListener {
            adoptarPet()
        }

        return binding.root
    }

    private fun adoptarPet() {
        agregarAAdoptados()
        removerDeAdoptables()
        removerDeFavoritos()
        navigateBack()
    }

    private fun navigateBack() {
        val navController = binding.root.findNavController()
        navController.popBackStack(R.id.nav_graph, false)
        navController.navigate(R.id.adopcion_fragment)
    }

    private fun agregarAAdoptados() {

    }

    private fun removerDeAdoptables() {

    }

    private fun removerDeFavoritos() {

    }
}