package com.example.beta.ui.view

// Correct import statements
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.beta.data.database.entities.Pet
import com.example.beta.databinding.FragmentPublicacionBinding
import com.example.beta.ui.viewmodel.PublicacionViewModel
import com.example.beta.util.Result
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PublicacionFragment : Fragment() {
    private var _binding: FragmentPublicacionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PublicacionViewModel by viewModels()

    companion object {
        fun newInstance() = PublicacionFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublicacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize an ArrayAdapter with an empty list
        val breedsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        // Assign the adapter to the AutoCompleteTextView
        binding.breedAutoComplete.setAdapter(breedsAdapter)

        // Observe LiveData for breeds
        viewModel.breedsLiveData.observe(viewLifecycleOwner) { breedsList ->
            breedsAdapter.clear()
            if (!breedsList.isNullOrEmpty()) {
                breedsAdapter.addAll(breedsList.map { it.breedName }) // Now you have a list
            }
            breedsAdapter.notifyDataSetChanged()
        }

        // Set an onClickListener to the breed dropdown to show the dropdown menu when clicked
        binding.breedDropdownContainer.setOnClickListener {
            binding.breedAutoComplete.showDropDown()
        }

        // Set an item click listener to handle what happens when a breed is selected
        binding.breedAutoComplete.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedBreed = adapterView.getItemAtPosition(position) as String
            // TODO: Based on the selected breed, fetch and display sub-breeds if any
        }

        binding.buttonConfirm.setOnClickListener {
            // Collect the data from the input fields
            val petName = binding.eTNombrePet.text.toString()
            val petBreed = binding.breedAutoComplete.text.toString()
            val petSubBreed = binding.subBreedAutoComplete.text.toString()
            val urlImage = "" // TODO: get image URL from image picker/upload
            val petAge = 5 // TODO: parse age from input
            val petGender = true // TODO: get gender from input, probably a boolean

            // Create Pet object
            val pet = Pet(
                petName = petName,
                petBreed = petBreed,
                petSubBreed = petSubBreed,
                urlImage = urlImage,
                petAge = petAge,
                petGender = petGender
            )

            // Call ViewModel to add Pet
            viewModel.addPet(pet) { result ->
                when (result) {
                    is Result.Success -> {
                        // Handle success, e.g., by showing a success message and/or navigating back
                    }
                    is Result.Error -> {
                        // Handle error, e.g., by showing an error message to the user
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leak
    }
}
