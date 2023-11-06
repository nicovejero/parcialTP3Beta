package com.example.beta.ui.view

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.ImageViewActivity
import com.example.beta.R
import com.example.beta.data.database.entities.Pet
import com.example.beta.data.database.entities.User
import com.example.beta.databinding.FragmentAdopcionDetailCarouselBinding
import com.example.beta.ui.holder.ImageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PetInAdoptionDetailFragment : Fragment() {
    private val args: PetInAdoptionDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentAdopcionDetailCarouselBinding
    private val pet: Pet by lazy { args.pet }
    private val uid: String? by lazy { FirebaseAuth.getInstance().currentUser?.uid }
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdopcionDetailCarouselBinding.inflate(inflater, container, false)
        binding.petDetailCallButton.setOnClickListener {
            // Phone number you want to call
            val phoneNumber = "123456789" // Replace with the number you want to call

            // Create an Intent to initiate the call
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phoneNumber"))
            Toast.makeText(requireActivity(), "tel:$phoneNumber", Toast.LENGTH_SHORT).show()
            // Start the dialer activity
            startActivity(intent)
        }

        val recyclerView: RecyclerView = binding.petDetailPicture
        val arrayList = ArrayList<String>()

        // Add image URLs to the array list
        arrayList.apply {
            add("https://images.unsplash.com/photo-1692528131755-d4e366b2adf0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzNXx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
            add("https://images.unsplash.com/photo-1692862582645-3b6fd47b7513?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw0MXx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
            add("https://images.unsplash.com/photo-1692584927805-d4096552a5ba?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw0Nnx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
            add("https://images.unsplash.com/photo-1692854236272-cc49076a2629?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw1MXx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
            add("https://images.unsplash.com/photo-1681207751526-a091f2c6a538?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyODF8fHxlbnwwfHx8fHw%3D&auto=format&fit=crop&w=500&q=60")
            add("https://images.unsplash.com/photo-1692610365998-c628604f5d9f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyODZ8fHxlbnwwfHx8fHw%3D&auto=format&fit=crop&w=500&q=60")
        }

        val adapter = ImageAdapter(requireContext(), arrayList)

        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : ImageAdapter.OnItemClickListener {
            override fun onClick(imageView: ImageView, path: String) {
                val intent = Intent(requireActivity(), ImageViewActivity::class.java)
                intent.putExtra("image", path)
                val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), imageView, "image").toBundle()
                startActivity(intent, options)
            }
        })


        val petName: TextView = binding.petDetailName
        //val petBreed: TextView = binding.petDetailBreed
        //val petSubBreed: TextView = binding.petDetailSubBreed
        val petWeight: TextView = binding.petDetailWeight
        //val petPicture = binding.petDetailPicture
        val petGender: TextView = binding.petDetailGender
        val petAge: TextView = binding.petDetailAge
        val petOwnerName = binding.petDetailOwnerName
        val petLocation = binding.nombreMascotaPetContainer2
        //val petOwnerPicture = binding.petDetailOwnerPicture

        petName.text = pet.petName
        //petBreed.text = petBreed.toString()
        //petSubBreed.text = petSubBreed.toString()
        petAge.text = pet.petAge.toString()
        petWeight.text = pet.petWeight.toString()
        petOwnerName.text = pet.petOwner
        petGender.text = pet.petGender.toString()
        petLocation.text = pet.petLocation

        binding.adoptButton.setOnClickListener {
            adoptarPet()
        }

        return binding.root
    }

    private fun adoptarPet() {
        uid?.let { uid ->
            db.collection("users").document(uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    user?.let {
                        val updatedAdoptedPets = it.adopted.toMutableList().apply {
                            add(pet) // Add the new pet to the current list
                        }
                        actualizarAdoptados(uid, updatedAdoptedPets) {
                            removerDeAdoptables()
                            removerDeFavoritos()
                            navigateBack()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireActivity(), "Error al recuperar información del usuario: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun navigateBack() {
        val navController = binding.root.findNavController()
        navController.popBackStack(R.id.nav_graph, false)
        navController.navigate(R.id.adopcion_fragment)
    }
    /*
        private fun navigateBack() {
            val navController = binding.root.findNavController()
            navController.navigate(R.id.action_petInAdoptionDetailFragment_to_adopcionFragment)
        }*/



    private fun actualizarAdoptados(userId: String, updatedAdoptedPets: List<Pet>, onSuccess: () -> Unit) {
        db.collection("users").document(userId)
            .update("adopted", updatedAdoptedPets)
            .addOnSuccessListener {
                Toast.makeText(requireActivity(), "Has adoptado a ${pet.petName}", Toast.LENGTH_SHORT).show()
                onSuccess()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireActivity(), "Error al actualizar la lista de adoptados: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removerDeAdoptables() {
        db.collection("pets").document(pet.petId).delete()
    }

    private fun removerDeFavoritos() {
        //db.collection("users").document(user.userId).collection("bookmarks").document(pet.petId!!).delete()
    }
}