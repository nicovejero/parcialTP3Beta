package com.example.beta

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.beta.data.database.entities.User
import com.example.beta.databinding.ActivityInicioBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


class InicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest
    private val RC_SIGN_IN = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        oneTapClient = Identity.getSignInClient(this)
        initializeSignInRequest()
        // Check if the user is already signed in
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            // If signed in, start MainActivity
            navigateToMainActivity()
        } else {
            // Not signed in, set up the login button to start sign-in flow
            binding.btnSignIn.setOnClickListener {
                // You can add the logic to initiate the sign-in process here.
                // For example, this could open a sign-in intent or dialog.
                showGoogleSignInDialog()
            }
        }
    }

    private fun navigateToMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        // Optionally, you might want to clear the activity stack
        finish()
    }

    private fun initializeSignInRequest() {
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun showGoogleSignInDialog() {
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    startIntentSenderForResult(
                        intentSenderRequest.intentSender,
                        RC_SIGN_IN,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("TAG", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                showNoGoogleAccountInDeviceWarning()
                Log.d("TAG", e.localizedMessage)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                handleSignInResult(data)
            }
        }
    }

    private fun handleSignInResult(data: Intent?) {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val userAuth = GoogleAuthProvider.getCredential(credential?.googleIdToken, null)
            val idToken = credential?.googleIdToken
            if (idToken != null) {
                val email = credential.id
                Toast.makeText(applicationContext, "token: $email", Toast.LENGTH_SHORT).show()
                firebaseAuth.signInWithCredential(userAuth)
                    .addOnCompleteListener(this) { task ->
                        Log.e("TAG", "signInWithCredential:onComplete:" + task.getResult().toString())
                        if (task.isSuccessful) {
                            checkAndCreateUserInFirestore()
                            Toast.makeText(applicationContext, "Usuario creado", Toast.LENGTH_SHORT).show()
                            navigateToMainActivity()
                            finish()
                        } else {
                            Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } catch (e: ApiException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndCreateUserInFirestore() {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            val db = FirebaseFirestore.getInstance()
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userRef = db.collection("users").document(uid)

            userRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        // User exists in Firestore, no need to create it
                    } else if (currentUser != null) {
                        Toast.makeText(applicationContext, "Usuario creado", Toast.LENGTH_SHORT).show()
                        // User doesn't exist, create a new document
                        val user = currentUser.displayName?.let { currentUser.email?.let { it1 -> User(userId = uid, firstName = it, email = it1, urlImage = currentUser.photoUrl.toString()) } }
                        if (user != null) {
                            userRef.set(user)
                        }
                    }
                } else {
                    // Handle the error
                    val exception = task.exception
                    if (exception != null) {
                        // Handle the error
                    }
                }
            }
        }
    }

    private fun showNoGoogleAccountInDeviceWarning() {
        AlertDialog.Builder(this)
            .setTitle("No registrado con Google")
            .setMessage("Esta aplicación requiere estar conectado a una cuenta de Google. Por favor, inicie sesión en Google.")
            .setNegativeButton("Salir") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setPositiveButton("Abrir configuración") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(android.provider.Settings.ACTION_ADD_ACCOUNT)
                intent.putExtra(android.provider.Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                recreate()
            }
            .setCancelable(false)
            .show()
    }
}
