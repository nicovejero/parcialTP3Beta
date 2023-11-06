package com.example.beta.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.ImageViewActivity
import com.example.beta.R
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.FragmentAdopcionDetailCarouselBinding
import com.example.beta.ui.holder.ImageAdapter
import com.example.beta.ui.viewmodel.PetInAdoptionDetailViewModel
import com.google.firebase.auth.FirebaseAuth

class PetInAdoptionDetailFragment : Fragment() {
    private val args: PetInAdoptionDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentAdopcionDetailCarouselBinding
    private val viewModel: PetInAdoptionDetailViewModel by viewModels()
    private val petModel: PetModel by lazy { args.pet }
    private val uid: String? by lazy { FirebaseAuth.getInstance().currentUser?.uid }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdopcionDetailCarouselBinding.inflate(inflater, container, false)
        setupUI()

        // After setting up the UI with petModel, immediately fetch image URLs
        viewModel.getImageUrlsForPet(petModel.petId)

        // Observe LiveData for image URLs to update RecyclerView
        viewModel.imageUrls.observe(viewLifecycleOwner) { imageUrls ->
            imageUrls?.let {
                setupRecyclerView(it)
            }
        }

        // Observe ViewModel LiveData for operation status
        viewModel.operationStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is PetInAdoptionDetailViewModel.OperationStatus.Success -> {
                    Toast.makeText(context, "Adoption successful!", Toast.LENGTH_SHORT).show()

                }
                is PetInAdoptionDetailViewModel.OperationStatus.Failure -> {
                    Toast.makeText(context, "Error: ${status.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    private fun setupUI() {
        // Setup UI elements with petModel data
        binding.petDetailName.text = petModel.petName
        binding.petDetailWeight.text = petModel.petWeight.toString()
        binding.petDetailGender.text = petModel.petGender
        binding.petDetailAge.text = petModel.petAge.toString()
        binding.petDetailOwnerName.text = petModel.petOwner
        binding.petDetailLocation.text = petModel.petLocation

        binding.petDetailCallButton.setOnClickListener {
            //initiateCall(petModel.contactNumber)
            initiateCall("123456789")
        }

        binding.adoptButton.setOnClickListener {
            uid?.let { userId ->
                viewModel.adoptPet(petModel, userId)
                viewModel.markPetAsAdopted(petModel.petId)
                val action = PetInAdoptionDetailFragmentDirections.actionGlobalToAdopcionFragment()
                findNavController().popBackStack(R.id.nav_graph, false)
                findNavController().navigate(action)
            } ?: Toast.makeText(requireContext(), "You must be logged in to adopt a pet", Toast.LENGTH_SHORT).show()
        }
        // Consider placing the following button click listeners here if not already defined

    }

    private fun setupRecyclerView(imageUrls: ArrayList<String>) {
        // Assuming ImageAdapter is the adapter for RecyclerView
        val adapter = ImageAdapter(requireContext(), imageUrls)
        val recyclerView: RecyclerView = binding.petDetailPicture
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : ImageAdapter.OnItemClickListener {
            override fun onClick(imageView: ImageView, path: String) {
                // The same code as before for click event
            }
        })
    }

    private fun initiateCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

}
