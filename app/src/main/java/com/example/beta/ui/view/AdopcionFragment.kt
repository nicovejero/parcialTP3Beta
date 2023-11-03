package com.example.beta.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beta.R
import com.example.beta.ui.viewmodel.AdopcionViewModel

class AdopcionFragment : Fragment() {

    companion object {
        fun newInstance() = AdopcionFragment()
    }

    private lateinit var viewModel: AdopcionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_adopcion, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdopcionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}