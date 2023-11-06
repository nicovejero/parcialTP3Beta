package com.example.beta.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.beta.data.database.adapter.PetAdoptableFirestoreAdapter
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.FragmentFavoritosBinding
import com.example.beta.ui.viewmodel.FavoritosViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldPath

class FavoritosFragment : Fragment() {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritosViewModel by viewModels()
    private var adapter: PetAdoptableFirestoreAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize your adapter and RecyclerView here if not already done

        // Now observe the LiveData for changes
        viewModel.userBookmarks.observe(viewLifecycleOwner) {
            adapter?.notifyDataSetChanged()
        }
        // Trigger loading of bookmarks
        viewModel.fetchBookmarkedPets(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        viewModel.userBookmarks.observe(viewLifecycleOwner, Observer { bookmarks ->
            // This check now covers both scenarios: no bookmarks or bookmarks present
            val hasBookmarks = !bookmarks.isNullOrEmpty()
            binding.recyclerViewCardsFavoritos.visibility = if (hasBookmarks) View.VISIBLE else View.GONE
            if (hasBookmarks && bookmarks != null) {
                setupRecyclerView(bookmarks)
            } else {
                showMessage("You have no bookmarked pets.")
            }
            // Here we update the adapter with the new bookmarks
            adapter?.updateUserBookmarks(bookmarks ?: listOf())
        })
    }

    private fun setupRecyclerView(bookmarkList: List<String>) {
        val query: Query = FirebaseFirestore.getInstance()
            .collection("pets")
            .whereIn(FieldPath.documentId(), bookmarkList)

        val options: FirestoreRecyclerOptions<PetModel> = FirestoreRecyclerOptions.Builder<PetModel>()
            .setQuery(query, PetModel::class.java)
            .setLifecycleOwner(this)
            .build()

        adapter = PetAdoptableFirestoreAdapter(options, userId)
        binding.recyclerViewCardsFavoritos.adapter = adapter
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter?.stopListening()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }
}
