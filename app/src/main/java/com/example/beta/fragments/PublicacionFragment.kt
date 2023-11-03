package com.example.beta.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beta.R
import com.example.beta.viewmodel.PublicacionViewModel

class PublicacionFragment : Fragment() {

    companion object {
        fun newInstance() = PublicacionFragment()
    }

    private lateinit var viewModel: PublicacionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_publicacion, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PublicacionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}