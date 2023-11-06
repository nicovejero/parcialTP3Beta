package com.example.beta.ui.view

import android.app.Activity
import android.content.Intent
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
import com.example.beta.data.model.PetModel
import com.example.beta.databinding.FragmentPublicacionBinding
import com.example.beta.ui.viewmodel.PublicacionViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PublicacionFragment : Fragment() {
    private var _binding: FragmentPublicacionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PublicacionViewModel by viewModels()
    private var  selectedImageViewId : Int = 0

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
            startImagePicker(R.id.simpleImageButton1)
        }
        binding.simpleImageButton2.setOnClickListener {
            startImagePicker(R.id.simpleImageButton2)
        }
        binding.simpleImageButton3.setOnClickListener {
            startImagePicker(R.id.simpleImageButton3)
        }
        binding.simpleImageButton4.setOnClickListener {
            startImagePicker(R.id.simpleImageButton4)
        }
        binding.simpleImageButton5.setOnClickListener {
            startImagePicker(R.id.simpleImageButton5)
        }
        return binding.root
    }

    private fun startImagePicker(imageViewId : Int){
        selectedImageViewId = imageViewId
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PublicacionFragment.PICK_IMAGE_REQUEST)
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
        val imageButton = view?.findViewById<ImageView>(selectedImageViewId)
        if (imageButton != null) {
            Glide.with(this).load(it).circleCrop().into(imageButton)
        }
    }

    private fun resetFields() {
        // Reset EditText fields
        binding.eTNombrePet.text = null
        binding.breedAutoComplete.setText("", false)
        binding.subBreedAutoComplete.setText("", false)
        binding.publicacionDescriptionInput.text = null
        binding.ageSpinner.clearListSelection()
        binding.locationsSpinner.clearListSelection()
        binding.pesoDropdownContainer.text = null
        binding.publicacionPhoneInput.text = null

        // Reset ImageViews or other image selectors
        // Assuming you have a method to clear the images or set to default
        resetImages()

        // Reset Spinners or AutoCompleteTextViews to default value
        binding.ageSpinner.clearListSelection()

        // Reset the switch to the default position
        binding.genderSwitch.isChecked = false
    }

    private fun resetImages() {
        // Clear the image selections here
        // Example to reset image on ImageView
        //Glide.with(this).clear(binding.profileImage)
        //binding.profileImage.setImageDrawable(null) // or set to a default image if desired
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

        viewModel.resetFields.observe(viewLifecycleOwner) { shouldReset ->
            // If you made resetFields nullable, then handle the potential null case here.
            shouldReset?.let {
                if (it) {
                    resetFields()
                    viewModel.onFieldsResetComplete()
                }
            }
        }

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
        val locations = listOf("CABA", "GBA", "Cordoba", "Rosario", "Mendoza", "Salta", "Tucuman", "Neuquen", "Mar del Plata", "La Plata", "Santa Fe", "San Juan", "San Luis", "Entre Rios", "Corrientes", "Misiones", "Chaco", "Formosa", "Jujuy", "La Rioja", "Santiago del Estero", "Catamarca", "Chubut", "Tierra del Fuego", "Santa Cruz").toList()
        val ageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ages.map { it.toString() })
        val locationsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, locations.map { it.toString() })

        binding.locationsSpinner.setAdapter(locationsAdapter)
        binding.ageSpinner.setAdapter(ageAdapter)

        // Set up the gender switch
        binding.genderSwitch.setOnCheckedChangeListener { _, isChecked ->
            // isChecked == true if the switch is in the "On" position
            val petGender = if(isChecked) "Male" else "Female"
            Toast.makeText(requireContext(), "gender: $petGender", Toast.LENGTH_SHORT).show()
            // Do something with the gender value if neede
        }

        //
        binding.confirmAdoptionButton.setOnClickListener {
            generatePetAndAdopt()
        }
    }

    private fun generatePetAndAdopt() {
        val petName = binding.eTNombrePet.text.toString()
        val petBreed = binding.breedAutoComplete.text.toString()
        val petSubBreed = binding.subBreedAutoComplete.text.toString()
        val petImages = binding.eTNombrePet.text.toString() //Reemplazar por urls de imagenes que carga el user
        val urlImage = ArrayList<String>()
        urlImage.add("https://www.insidedogsworld.com/wp-content/uploads/2016/03/Dog-Pictures.jpg")
        urlImage.add("https://inspirationseek.com/wp-content/uploads/2016/02/Cute-Dog-Images.jpg")
        // Get the selected pet age from the spinner
        val petAge = binding.ageSpinner.text.toString().toInt()
        // Get the selected gender from the switch
        val petGender = binding.genderSwitch.isChecked
        val petWeight = binding.pesoDropdownContainer.text.toString().toDouble()
        // Create Pet object
        val petModel = PetModel(
            "",
            petName = petName,
            petBreed = petBreed,
            petSubBreed = petSubBreed,
            urlImage = urlImage,
            petAge = petAge,
            petWeight = petWeight,
            petGender = petGender
        )
        // Call ViewModel to add Pet
        viewModel.addPet(petModel)
    }
}
