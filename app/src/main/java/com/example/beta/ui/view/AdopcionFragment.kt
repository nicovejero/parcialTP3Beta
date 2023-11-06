package com.example.beta.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.beta.data.database.adapter.PetAdoptableFirestoreAdapter
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.FragmentAdopcionesListBinding
import com.example.beta.ui.viewmodel.AdopcionViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class AdopcionFragment : Fragment() {
    private var _binding: FragmentAdopcionesListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdopcionViewModel by viewModels()
    private var adapter: PetAdoptableFirestoreAdapter? = null
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdopcionesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch and subscribe to adopted pets when the view is created
        viewModel.fetchAdoptedPets()

        viewModel.adoptedPetsQuery.observe(viewLifecycleOwner) { query ->
            if (query != null) {
                val options = FirestoreRecyclerOptions.Builder<PetModel>()
                    .setQuery(query, PetModel::class.java)
                    .setLifecycleOwner(viewLifecycleOwner)
                    .build()
                adapter?.updateOptions(options) ?: run {
                    adapter = PetAdoptableFirestoreAdapter(options, userId, viewLifecycleOwner.lifecycle)
                    binding.recyclerViewAdopciones.adapter = adapter
                }
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
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter?.stopListening()
        _binding = null
    }
}
