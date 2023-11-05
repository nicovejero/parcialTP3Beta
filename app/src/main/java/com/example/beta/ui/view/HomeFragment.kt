package com.example.beta.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.data.database.adapter.FilterChipAdapter
import com.example.beta.data.database.adapter.PetAdoptableFirestoreAdapter
import com.example.beta.data.database.entities.Pet
import com.example.beta.data.model.ChipModel
import com.example.beta.ui.viewmodel.HomeViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.beta.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var petAdapter: PetAdoptableFirestoreAdapter
    private lateinit var filterAdapter: FilterChipAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var recyclerViewAdoption: RecyclerView
    private lateinit var recyclerViewFilter: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val query = db.collection("pets")
        recyclerViewAdoption = binding.cardsRecyclerView
        recyclerViewFilter = binding.chipsRecyclerView

        // Initialize the adapter for the pet cards with a Firestore query
        val petOptions = FirestoreRecyclerOptions.Builder<Pet>()
            .setQuery(query, Pet::class.java)
            .build()
        petAdapter = PetAdoptableFirestoreAdapter(petOptions)

        // Set up the RecyclerView for pet cards
        recyclerViewAdoption.adapter = petAdapter
        recyclerViewAdoption.layoutManager = LinearLayoutManager(requireContext())


        // Set up the RecyclerView for filter chips, initially with an empty list
        recyclerViewFilter.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterAdapter = FilterChipAdapter(emptyList()) { selectedBreeds ->
            updatePetQuery(selectedBreeds)
        }
        recyclerViewFilter.adapter = filterAdapter

        // Populate the filter chips by fetching the pet breeds
        fetchPetBreedsAndPopulateFilter()

        return binding.root
    }


    private fun fetchPetBreedsAndPopulateFilter() {
        db.collection("pets")
            .get()
            .addOnSuccessListener { result ->
                val breeds = result.mapNotNull { it.getString("breed") }.toSet()
                val chipModels = breeds.map { breed -> ChipModel(id = breed.hashCode(), text = breed) }
                Log.e("HomeFragment", "Chip models: $chipModels")
                filterAdapter.updateChips(chipModels) // Assuming FilterChipAdapter has a method to update the chips
            }
            .addOnFailureListener { exception ->
                Log.e("HomeFragment", "Error getting documents: ", exception)
            }
    }

    private fun updatePetQuery(selectedBreeds: Set<String>) {
        val newQuery = if (selectedBreeds.isEmpty()) {
            // If no breeds are selected, use the original query
            db.collection("pets")
        } else {
            // If breeds are selected, adjust the query to filter by those breeds
            db.collection("pets").whereIn("breed", selectedBreeds.toList())
        }

        // Build the new options for the FirestoreRecyclerAdapter
        val newOptions = FirestoreRecyclerOptions.Builder<Pet>()
            .setQuery(newQuery, Pet::class.java)
            .build()

        // Update the adapter with the new options
        petAdapter.updateOptions(newOptions)
    }


    override fun onStart() {
        super.onStart()
        petAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        petAdapter.stopListening()
    }

}