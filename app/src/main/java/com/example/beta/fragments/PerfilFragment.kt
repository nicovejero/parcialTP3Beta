package com.example.beta.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beta.R
import com.example.beta.viewmodel.PerfilViewModel

class PerfilFragment : Fragment() {

    companion object {
        fun newInstance() = PerfilFragment()
    }

    private lateinit var viewModel: PerfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        // TODO: Use the ViewModel
    }

}