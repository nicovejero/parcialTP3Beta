package com.example.beta.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.beta.data.database.adapter.PetAdoptableFirestoreAdapter
import com.example.beta.data.database.entities.Pet
import com.example.beta.databinding.FragmentFavoritosBinding
import com.example.beta.ui.viewmodel.FavoritosViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class FavoritosFragment : Fragment() {
    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritosViewModel by viewModels()
    private var adapter: PetAdoptableFirestoreAdapter? = null
    private val userId : String = "sQs8W7nRyTZkwZ1MGCYJ3UkGnWV2"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch and subscribe to bookmarked pets when the view is created
        viewModel.fetchBookmarkedPets()

        viewModel.bookmarkedPetsQuery.observe(viewLifecycleOwner) { query ->
            if (query != null) {
                val options = FirestoreRecyclerOptions.Builder<Pet>()
                    .setQuery(query, Pet::class.java)
                    .setLifecycleOwner(this@FavoritosFragment)
                    .build()
                adapter?.updateOptions(options) ?: run {
                    adapter = PetAdoptableFirestoreAdapter(options, userId)
                    binding.recyclerViewCardsFavoritos.adapter = adapter
                }
            }
        }

        viewModel.noBookmarksAvailable.observe(viewLifecycleOwner) { noBookmarks ->
            if (noBookmarks) {
                // Show message or adjust UI for no bookmarks.
                showMessage("You have no bookmarked pets.")
                // Adjust RecyclerView visibility or other UI elements if necessary
                binding.recyclerViewCardsFavoritos.visibility = View.GONE
            } else {
                // Hide the message or adjust the UI to show bookmarks.
                binding.recyclerViewCardsFavoritos.visibility = View.VISIBLE
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        // Start listening to changes in the Firestore
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        // Stop listening to changes in the Firestore
        adapter?.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up resources and listeners
        adapter?.stopListening()
        _binding = null
    }
}
