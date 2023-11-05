package com.example.beta.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beta.R
import com.example.beta.databinding.FragmentAdopcionDetailBinding
import com.example.beta.databinding.FragmentDetailBinding
import com.example.beta.ui.viewmodel.AdopcionViewModel


class AdopcionFragment : Fragment() {

    companion object {
        fun newInstance() = AdopcionFragment()
    }

    private lateinit var viewModel: AdopcionViewModel
    private lateinit var binding : FragmentAdopcionDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdopcionDetailBinding.inflate(layoutInflater, container, false)
        binding.callButton.setOnClickListener{
            // Phone number you want to call
            val phoneNumber = "123456789" // Replace with the number you want to call

            // Create an Intent to initiate the call
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phoneNumber"))
            Toast.makeText(requireActivity(), "tel:$phoneNumber", Toast.LENGTH_SHORT).show()

            // Start the dialer activity
            startActivity(intent)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdopcionViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
