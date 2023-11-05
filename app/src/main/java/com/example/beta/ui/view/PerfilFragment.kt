package com.example.beta.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.beta.InicioActivity
import com.example.beta.MainActivity
import com.example.beta.R
import com.example.beta.data.database.entities.User
import com.example.beta.databinding.FragmentPerfilBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilFragment : Fragment() {
    private lateinit var binding: FragmentPerfilBinding
    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid!!
    private val userRef = db.collection("users").document(uid)

    private val mGoogleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(requireContext(), gso)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        populateUser()
    }

    private fun populateUser() {
        userRef.get().addOnSuccessListener {
            if (it.exists()) {
                val user = it.toObject(User::class.java)
                binding.profileName.text = user?.firstName
                binding.profileEmail.text = user?.email
                Glide
                    .with(binding.root.context)
                    .load(user?.urlImage)
                    .into(binding.profileImage)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerfilBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), InicioActivity::class.java)
                Toast.makeText(requireContext(), mGoogleSignInClient.toString(), Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = view?.findNavController()
        val action = SettingFragmentDirections.actionGlobalNavDrawerGoBack()


        binding.goBackImage.setOnClickListener{

            if (navController != null) {
                navController.popBackStack(R.id.nav_graph, false)
            }

            if (activity is MainActivity) {
                (activity as MainActivity).setBottomNavViewVisibility(View.VISIBLE)
                (activity as MainActivity).supportActionBar?.show()
            }

            navController?.navigate(action)
            true
        }
        super.onViewCreated(view, savedInstanceState)
    }

}