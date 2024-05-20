package com.nhtruong.coffee.ViewModel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UpdatePasswordViewModel : ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val _passwordUpdated = MutableLiveData<Boolean>()
    val passwordUpdated: LiveData<Boolean> = _passwordUpdated

    fun updatePassword(newPassword: String, showToast: (String) -> Unit) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // Update the password in Firebase Realtime Database
                        val userId = user.uid
                        val userRef = database.getReference("Users").child(userId)
                        userRef.child("password").setValue(newPassword)
                            .addOnSuccessListener {
                                _passwordUpdated.value = true
                            }
                            .addOnFailureListener { exception ->
                                _passwordUpdated.value = false

                            }
                    } else {
                        _passwordUpdated.value = false
                        showToast("Failed to update password: ${task.exception?.message}")
                    }
                }
        }
    }
}
