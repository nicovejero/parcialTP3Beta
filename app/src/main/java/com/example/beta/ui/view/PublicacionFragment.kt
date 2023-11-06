package com.example.beta.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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
        val subBreedsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())

        // Assign the adapter to the AutoCompleteTextView
        binding.breedAutoComplete.setAdapter(breedsAdapter)
        binding.subBreedAutoComplete.setAdapter(subBreedsAdapter)
        binding.subBreedAutoComplete.showDropDown()

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

        viewModel.subBreedsLiveData.observe(viewLifecycleOwner) { subBreedsList ->
            subBreedsAdapter.clear()
            if (subBreedsList.isNotEmpty()) {
                subBreedsAdapter.addAll(subBreedsList)
                binding.subBreedDropdownContainer.visibility = View.VISIBLE
            } else {
                binding.subBreedDropdownContainer.visibility = View.GONE
            }
            subBreedsAdapter.notifyDataSetChanged() // Notify the adapter
        }

        binding.breedAutoComplete.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedBreed = adapterView.getItemAtPosition(position) as String
            viewModel.onBreedSelected(selectedBreed)
        }

        // Set up the age spinner
        val ages = (1..20).toList() // Replace with your age range
        val ageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ages.map { it.toString() })
        binding.ageSpinner.setAdapter(ageAdapter)

        // Set up the gender switch
        binding.genderSwitch.setOnCheckedChangeListener { _, isChecked ->
            // isChecked == true if the switch is in the "On" position
            val petGender = if(isChecked) "Male" else "Female"
            Toast.makeText(requireContext(), "gender: $petGender", Toast.LENGTH_SHORT).show()
            // Do something with the gender value if neede
        }


        binding.buttonConfirm.setOnClickListener {
            // Collect the data from the input fields
            val petName = binding.eTNombrePet.text.toString()
            val petBreed = binding.breedAutoComplete.text.toString()
            val petSubBreed = binding.subBreedAutoComplete.text.toString()
            val urlImage = "https://www.insidedogsworld.com/wp-content/uploads/2016/03/Dog-Pictures.jpg" // TODO: get image URL from image picker/upload
            // Get the selected pet age from the spinner
            val petAge = binding.ageSpinner.listSelection
            // Get the selected gender from the switch
            val petGender = binding.genderSwitch.isChecked

            // Create Pet object
            val pet = Pet(
                petName = petName,
                petBreed = petBreed,
                petSubBreed = petSubBreed,
                urlImage = urlImage,
                petAge = petAge,
                petGender = petGender,
            )

            // Call ViewModel to add Pet
            viewModel.addPet(pet) { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), "Mascota puesta en adopción", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Error -> {
                        // Handle error, e.g., by showing an error message to the user
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leak
    }
}
