package com.example.beta.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beta.R
import com.example.beta.ui.viewmodel.AdopcionViewModel
import com.example.beta.ui.viewmodel.FavoritosViewModel

class FavoritosFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritosFragment()
    }

    private lateinit var viewModel: FavoritosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favoritos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavoritosViewModel::class.java)
        // TODO: Use the ViewModel
    }

}