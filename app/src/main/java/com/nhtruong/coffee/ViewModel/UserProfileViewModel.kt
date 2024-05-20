package com.nhtruong.coffee.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.nhtruong.coffee.model.UserModel

class UserProfileViewModel : ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun getUserData(updateUI: (UserModel) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        database.getReference("Users").child(userId).get().addOnSuccessListener {
            val user = it.getValue(UserModel::class.java)
            user?.let { userModel ->
                updateUI(userModel)
            }
        }
    }

    fun updateUserData(name: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid ?: return
        val userMap = mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,

        )
        database.getReference("Users").child(userId).updateChildren(userMap)

    }




}
