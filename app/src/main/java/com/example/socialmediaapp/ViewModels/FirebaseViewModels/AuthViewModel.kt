package com.example.socialmediaapp.ViewModels.FirebaseViewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.DataModels.User
import com.example.socialmediaapp.HomeFeedActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class AuthViewModel(): ViewModel() {

    // Authentication Methods start

    private var auth: FirebaseAuth = Firebase.auth

    //Use livedata to hold the current user's information
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _authenticationState = MutableLiveData<AutheticationState>()
    val authenticationState: LiveData<AutheticationState> = _authenticationState

    fun handleLogin ( Email: String, Password: String, context: Context) {
        auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener( OnCompleteListener
        { task ->
            if (task.isSuccessful) {


                _authenticationState.value = AutheticationState.AUTHENTICATED
            } else {
                task.exception?.localizedMessage?.let { Log.e("Login Error", it) }
                _authenticationState.value = AutheticationState.UNAUTHENTICATED
            }

        })

    }

    fun handleSignUp (Username: String, Email: String, Password: String, context:Context) {

        auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val newUser = User(auth.currentUser?.uid ?: "", Username, Email, Password, "" )
                _currentUser.value = newUser
                _authenticationState.value = AutheticationState.REGISTERED
                Log.d("Sign up Complete","Sign up Complete: ${task.exception?.localizedMessage}" )
            } else {
                Log.e("Sign up Failed","Sign up Failure: ${task.exception?.localizedMessage}")
                _authenticationState.value = AutheticationState.UNAUTHENTICATED
            }
        }
    }

    fun clearCurrentUser() {
        _currentUser.value = null
    }



    fun handleLoginWithGoogle() {

    }

    fun handleSignUpWithGoogle() {


    }

}






//Authentication End


//Add to realtime database

