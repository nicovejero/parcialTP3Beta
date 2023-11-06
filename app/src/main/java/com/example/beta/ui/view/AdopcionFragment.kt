package com.example.beta.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beta.databinding.FragmentAdopcionesListBinding
import com.example.beta.ui.viewmodel.AdopcionViewModel


class AdopcionFragment : Fragment() {

    companion object {
        fun newInstance() = AdopcionFragment()
    }

    private lateinit var viewModel: AdopcionViewModel
    private lateinit var binding : FragmentAdopcionesListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdopcionesListBinding.inflate(layoutInflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdopcionViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
