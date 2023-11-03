package com.example.beta.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class InicioViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val authenticationState = MutableLiveData<AuthenticationState>()
    val signInStatus = MutableLiveData<SignInStatus>()
    private val _showGoogleSignInDialog = Channel<Unit>(Channel.CONFLATED)
    val showGoogleSignInDialog = _showGoogleSignInDialog.receiveAsFlow()

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    enum class SignInStatus {
        SUCCESS, FAILURE, IN_PROGRESS, NEEDS_ACCOUNT
    }

    init {
        // Check if user is logged in and update the authentication state
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            authenticationState.value = AuthenticationState.AUTHENTICATED
        } else {
            authenticationState.value = AuthenticationState.UNAUTHENTICATED
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        signInStatus.value = SignInStatus.IN_PROGRESS
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    authenticationState.value = AuthenticationState.AUTHENTICATED
                    signInStatus.value = SignInStatus.SUCCESS
                    checkAndCreateUserInFirestore()
                } else {
                    // If sign in fails, display a message to the UI.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                    signInStatus.value = SignInStatus.FAILURE
                }
            }
    }

    fun initiateGoogleSignIn() {
        viewModelScope.launch {
            _showGoogleSignInDialog.send(Unit)
        }
    }

    private fun checkAndCreateUserInFirestore() {
        val uid = firebaseAuth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(uid ?: "")

        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    // User exists in Firestore, no need to create it
                    Log.d(TAG, "User already exists in Firestore.")
                } else {
                    // User doesn't exist, create a new document
                    val user = hashMapOf(
                        "userId" to uid,
                        "email" to firebaseAuth.currentUser?.email,
                        "displayName" to firebaseAuth.currentUser?.displayName
                        // Add other user information you want to save
                    )
                    userRef.set(user).addOnSuccessListener {
                        Log.d(TAG, "User created in Firestore.")
                    }.addOnFailureListener {
                        Log.w(TAG, "Error adding user to Firestore", it)
                    }
                }
            } else {
                Log.w(TAG, "Error getting user from Firestore", task.exception)
            }
        }
    }

    companion object {
        private const val TAG = "InicioViewModel"

    }
}
