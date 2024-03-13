package com.example.socialmediaapp.ViewModels.FirebaseViewModels

import com.example.socialmediaapp.DataModels.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRepository(private val firebaseAuth: FirebaseAuth, private val database: FirebaseDatabase,) {


    fun createUser(email: String, password: String, username: String, onComplete: (Boolean, String) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(firebaseAuth.currentUser?.uid ?: "", username, password, email, "")
                    database.reference.child("Users").child(user.Uid).setValue(user)
                        .addOnCompleteListener { dbTask ->
                            onComplete(dbTask.isSuccessful, "")
                        }
                } else {
                    onComplete(false, task.exception?.message ?: "An error occurred")
                }
            }
    }
}