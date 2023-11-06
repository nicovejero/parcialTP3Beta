package com.example.beta.ui.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.beta.databinding.FragmentPublicacionBinding
import com.example.beta.domain.model.Breed
import com.example.beta.ui.viewmodel.PublicacionViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PublicacionFragment : Fragment() {
    private var _binding: FragmentPublicacionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PublicacionViewModel by viewModels()
    private var selectedImageViewId: Int = 0
    private val db = FirebaseFirestore.getInstance()

    companion object {
        fun newInstance() = PublicacionFragment()
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPublicacionBinding.inflate(inflater, container, false)
        setupImageButtons()
        return binding.root
    }

    private fun setupImageButtons() {
        listOf(
            binding.simpleImageButton1,
            binding.simpleImageButton2,
            binding.simpleImageButton3,
            binding.simpleImageButton4,
            binding.simpleImageButton5
        ).forEach { button ->
            button.setOnClickListener { startImagePicker(it.id) }
        }
    }

    private fun startImagePicker(imageViewId: Int) {
        selectedImageViewId = imageViewId
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            startActivityForResult(it, PICK_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                binding.run {
                    val imageView = when (selectedImageViewId) {
                        simpleImageButton1.id -> simpleImageButton1
                        simpleImageButton2.id -> simpleImageButton2
                        simpleImageButton3.id -> simpleImageButton3
                        simpleImageButton4.id -> simpleImageButton4
                        simpleImageButton5.id -> simpleImageButton5
                        else -> null
                    }
                    imageView?.let { Glide.with(this@PublicacionFragment).load(uri).into(it) }
                }
            }
        }
    }

    private fun guardarImagen(uri: Uri) {
        view?.findViewById<ImageView>(selectedImageViewId)?.let { imageView ->
            Glide.with(this).load(uri).circleCrop().into(imageView)
        }
    }

    private fun resetFields() {
        with(binding) {
            eTNombrePet.text = null
            breedAutoComplete.setText("", false)
            subBreedAutoComplete.setText("", false)
            publicacionDescriptionInput.text = null
            ageSpinner.clearListSelection()
            locationsSpinner.clearListSelection()
            pesoDropdownContainer.text = null
            publicacionPhoneInput.text = null
            genderSwitch.isChecked = false
        }
        resetImages()
    }

    private fun resetImages() {
        // Implement the logic to reset images
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            loadBreeds()
            setupObservers()
            initializeAdapters()
            handleGenderSwitch()
            setupButtonListeners()
        }
    }

    private fun setupObservers() {
        // Observe breeds LiveData
        viewModel.breedsLiveData.observe(viewLifecycleOwner) { breedsList ->
            updateBreedsList(breedsList)
        }

        // Observe error LiveData
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                // Handle the error message
            }
        }

        // Observe resetFields LiveData
        viewModel.resetFields.observe(viewLifecycleOwner) { shouldReset ->
            if (shouldReset == true) {
                resetFields()
                viewModel.onFieldsResetComplete()
            }
        }

        // Observe subBreedsLiveData LiveData
        viewModel.subBreedsLiveData.observe(viewLifecycleOwner) { subBreedsList ->
            updateSubBreedsList(subBreedsList)
        }
    }

    private fun updateBreedsList(breedsList: List<Breed>) {
        val adapter = (binding.breedAutoComplete.adapter as ArrayAdapter<String>)
        adapter.clear()
        adapter.addAll(breedsList.map { it.breedName })
        adapter.notifyDataSetChanged()
    }

    private fun updateSubBreedsList(subBreedsList: List<String>) {
        val adapter = (binding.subBreedAutoComplete.adapter as ArrayAdapter<String>)
        adapter.clear()
        adapter.addAll(subBreedsList)
        adapter.notifyDataSetChanged()
        binding.subBreedDropdownContainer.visibility = if (subBreedsList.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun PublicacionViewModel.initializeAdapters() {
        val context = requireContext()
        binding.breedAutoComplete.setAdapter(
            ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line)
        )
        binding.subBreedAutoComplete.setAdapter(
            ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line)
        )
        // Initialize other adapters here
    }

    private fun PublicacionViewModel.handleGenderSwitch() {
        binding.genderSwitch.setOnCheckedChangeListener { _, isChecked ->
            val petGender = if (isChecked) "Macho" else "Hembra"
            Toast.makeText(requireContext(), "Gender: $petGender", Toast.LENGTH_SHORT).show()
        }
    }

    private fun PublicacionViewModel.setupButtonListeners() {
        binding.confirmAdoptionButton.setOnClickListener {
            generatePetInfo()?.let { petInfo ->
                // TODO: Implement database operation to add pet info
            }
        }
    }

    private fun generatePetInfo(): Map<String, Any>? {
        // TODO: Implement the logic to generate pet info
        return null
    }

    // Helper function to collect image URLs from the ImageViews or storage
    // TODO: Implement collectImageUrls()
    // private fun collectImageUrls(): ArrayList<String> {
    //     val urls = ArrayList<String>()
    //     // Add logic to collect the actual image URLs
    //     return urls
    // }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
