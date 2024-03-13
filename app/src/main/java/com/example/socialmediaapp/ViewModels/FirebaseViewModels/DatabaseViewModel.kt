package com.example.socialmediaapp.ViewModels.FirebaseViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.DataModels.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class DatabaseViewModel: ViewModel() {

    private var database: FirebaseDatabase = Firebase.database
    fun createUser(user: User) {
        val userID = FirebaseAuth.getInstance().currentUser?.uid ?: return

        database.reference.child("Users").child(userID).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("UserViewModel", "User saved to the database")
            } else {
                Log.e("UserViewModel", "Failed to save user: ${task.exception?.message}")
            }
        }
    }
}