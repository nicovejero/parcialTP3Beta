package com.example.beta.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.data.database.adapter.FilterChipAdapter
import com.example.beta.data.database.adapter.PetAdoptableFirestoreAdapter
import com.example.beta.data.model.ChipModel
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.FragmentHomeBinding
import com.example.beta.ui.viewmodel.HomeViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var petAdapter: PetAdoptableFirestoreAdapter
    private lateinit var filterAdapter: FilterChipAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setupRecyclerViews()
        setupObservers()
        viewModel.fetchPetBreeds()
        return binding.root
    }

    private fun setupRecyclerViews() {
        val initialQuery = viewModel.getInitialPetQuery()

        val options = FirestoreRecyclerOptions.Builder<PetModel>()
            .setQuery(initialQuery, PetModel::class.java)
            .build()

        petAdapter = PetAdoptableFirestoreAdapter(options, viewModel.getUserId())
        binding.cardsRecyclerView.adapter = petAdapter
        binding.cardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        filterAdapter = FilterChipAdapter(emptyList()) { breed ->
            viewModel.updateSelectedBreeds(breed) // Ensure this matches the expected parameter type for the method.
        }
        binding.chipsRecyclerView.adapter = filterAdapter
        binding.chipsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


    private fun setupObservers() {
        viewModel.petBreeds.observe(viewLifecycleOwner) { breeds ->
            filterAdapter.updateChips(breeds.map { breed ->
                ChipModel(
                    id = breed.hashCode(),
                    text = breed
                )
            })
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

        viewModel.petOptions.observe(viewLifecycleOwner) { options ->
            petAdapter.updateOptions(options)
        }
    }

    override fun onStart() {
        super.onStart()
        petAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        petAdapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        petAdapter.stopListening() // Ensure you stop listening when the fragment view is destroyed
    }
}