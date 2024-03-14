package com.example.socialmediaapp.ViewModels.FirebaseViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.DataModels.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseViewModel: ViewModel() {

    private var database: FirebaseDatabase = Firebase.database
    private var fireStoreDB: FirebaseFirestore = FirebaseFirestore.getInstance()
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

    // Fire Store Data

    fun createUserWithFireStore(user: User) {

        val userData = hashMapOf(
            "Uid" to user.Uid,
            "Username" to user.UserName,
            "Email" to user.Email,
            "Password" to user.Password,
            "ProfilePicture" to user.profilePicture,
        )

        fireStoreDB.collection("Users")
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d("UserViewModel, FireStore", "User saved to FireStore: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.e("UserViewModel, FireStore", "User failed to save to Firestore: $e")
            }
    }
}