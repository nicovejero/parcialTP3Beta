package com.example.beta.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beta.data.database.entities.User

class SharedViewModel : ViewModel() {
    private val currentUser: MutableLiveData<User> = MutableLiveData()

    fun setUser(user: User) {
        currentUser.value = user
    }

    fun getUser(): LiveData<User> {
        return currentUser
    }
}