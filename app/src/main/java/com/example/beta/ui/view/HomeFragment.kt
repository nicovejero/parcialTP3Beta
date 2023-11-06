package com.example.beta.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.data.database.adapter.FilterChipAdapter
import com.example.beta.data.database.adapter.PetAdoptableFirestoreAdapter
import com.example.beta.data.model.PetModel
import com.example.beta.data.model.ChipModel
import com.example.beta.ui.viewmodel.HomeViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.beta.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FieldPath

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var petAdapter: PetAdoptableFirestoreAdapter
    private lateinit var filterAdapter: FilterChipAdapter
    private lateinit var recyclerViewAdoption: RecyclerView
    private lateinit var recyclerViewFilter: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container, false)
        val query = db.collection("pets").whereEqualTo("adopted", false)
        recyclerViewAdoption = binding.cardsRecyclerView
        recyclerViewFilter = binding.chipsRecyclerView

        // Initialize the adapter for the pet cards with a Firestore query
        val petModelOptions = FirestoreRecyclerOptions.Builder<PetModel>()
            .setQuery(query, PetModel::class.java)
            .build()
        petAdapter = PetAdoptableFirestoreAdapter(petModelOptions, uid!!, viewLifecycleOwner.lifecycle)

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
        val newOptions = FirestoreRecyclerOptions.Builder<PetModel>()
            .setQuery(newQuery, PetModel::class.java)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}