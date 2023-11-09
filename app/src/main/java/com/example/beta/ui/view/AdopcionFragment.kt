package com.example.beta.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.data.database.adapter.PetOwnerFirestoreAdapter
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.FragmentAdopcionesListBinding
import com.example.beta.ui.viewmodel.AdopcionViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class AdopcionFragment : Fragment() {
    private lateinit var binding: FragmentAdopcionesListBinding
    private val viewModel: AdopcionViewModel by viewModels()
    private lateinit var adapter: PetOwnerFirestoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdopcionesListBinding.inflate(inflater, container, false)
        setupRecyclerViews()
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.petOptions.observe(viewLifecycleOwner) { options ->
            adapter.updateOptions(options)
        }
    }
    private fun setupRecyclerViews() {
        val initialQuery = viewModel.getInitialPetQuery()

        val options = FirestoreRecyclerOptions.Builder<PetModel>()
            .setQuery(initialQuery, PetModel::class.java)
            .build()

        adapter = PetOwnerFirestoreAdapter(options)
        binding.recyclerViewAdopciones.adapter = adapter
        binding.recyclerViewAdopciones.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getInitialPetQuery()
        viewModel.adoptedPetsQuery.observe(viewLifecycleOwner) { query ->
            if (query != null) {
                val options = FirestoreRecyclerOptions.Builder<PetModel>()
                    .setQuery(query, PetModel::class.java)
                    .setLifecycleOwner(viewLifecycleOwner)
                    .build()
                adapter.updateOptions(options)
            }
        }

        viewModel.noAdoptionsAvailable.observe(viewLifecycleOwner) { noAdoptions ->
            if (noAdoptions) {
                showMessage("You have no adopted pets.")
                binding.recyclerViewAdopciones.visibility = View.GONE
            } else {
                binding.recyclerViewAdopciones.visibility = View.VISIBLE
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.stopListening()
    }
}
