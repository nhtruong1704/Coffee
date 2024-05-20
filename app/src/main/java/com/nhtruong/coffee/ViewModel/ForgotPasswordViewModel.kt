package com.nhtruong.coffee.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nhtruong.coffee.model.UserModel

class ForgotPasswordViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun checkEmailExists(email: String, onEmailChecked: (Boolean) -> Unit) {
        val userRef = firebaseDatabase.getReference("Users")
        val query = userRef.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Email exists in the database
                    onEmailChecked(true)
                } else {
                    // Email does not exist in the database
                    onEmailChecked(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ForgotPasswordViewModel", "Error checking email: ${error.message}")
            }
        })
    }

    fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
    }

}