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
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var petAdapter: PetAdoptableFirestoreAdapter
    private lateinit var filterAdapter: FilterChipAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerViews()
        setupObservers()
        viewModel.fetchPetBreeds()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the LiveData for pet breeds
        viewModel.petBreeds.observe(viewLifecycleOwner) { breeds ->
            // Update your filter chips UI with the breeds list
            filterAdapter.updateChips(breeds.map { breed ->
                ChipModel(
                    id = breed.hashCode(),
                    text = breed
                )
            })
        }

        // Observe the LiveData for error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            // Show the error message to the user, e.g. using a Toast
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

        // Observe the LiveData for FirestoreRecyclerOptions
        viewModel.petOptions.observe(viewLifecycleOwner) { newOptions ->
            // Update the adapter with new options
            petAdapter.updateOptions(newOptions)
        }

    }

    private fun setupRecyclerViews() {
        // Initialize the adapter for the pet cards with an empty FirestoreRecyclerOptions
        val options = FirestoreRecyclerOptions.Builder<PetModel>()
            .setQuery(viewModel.getInitialPetQuery(), PetModel::class.java)
            .build()

        petAdapter = PetAdoptableFirestoreAdapter(options, viewModel.getUserId())
        binding.cardsRecyclerView.adapter = petAdapter
        binding.cardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        filterAdapter = FilterChipAdapter(emptyList(), viewModel::updateSelectedBreeds)
        binding.chipsRecyclerView.adapter = filterAdapter
        binding.chipsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupObservers() {
        // Observe the breeds LiveData from ViewModel
        viewModel.breeds.observe(viewLifecycleOwner) { breeds ->
            filterAdapter.updateChips(breeds)
        }

        // Observe the pet query LiveData from ViewModel
        viewModel.petQuery.observe(viewLifecycleOwner) { query ->
            val newOptions = FirestoreRecyclerOptions.Builder<PetModel>()
                .setQuery(query, PetModel::class.java)
                .build()
            petAdapter.updateOptions(newOptions)
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
        _binding = null
    }
}
