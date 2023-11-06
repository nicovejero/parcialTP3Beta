package com.example.beta.ui.view

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.beta.R
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
         private const val PICK_IMAGE_REQUEST = 1
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

        binding.simpleImageButton1.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PublicacionFragment.PICK_IMAGE_REQUEST)

        }
        binding.simpleImageButton2.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PublicacionFragment.PICK_IMAGE_REQUEST)
        }
        binding.simpleImageButton3.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PublicacionFragment.PICK_IMAGE_REQUEST)
        }
        binding.simpleImageButton4.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PublicacionFragment.PICK_IMAGE_REQUEST)
        }
        binding.simpleImageButton5.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PublicacionFragment.PICK_IMAGE_REQUEST)
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PublicacionFragment.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Get the image URI from the result
            val imageUri = data?.data
            imageUri?.let {
                //uploadImageToFirebaseStorage(it)
                guardarImagen(it)

            }
        }
    }

    private fun guardarImagen(it: Uri) {
        val imageButton = view?.findViewById<ImageView>(R.id.profile_image)
        if (imageButton != null) {
            Glide.with(this).load(it).circleCrop().into(imageButton)
        }
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
            val urlImage = "" // TODO: get image URL from image picker/upload
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
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leak
    }
}
