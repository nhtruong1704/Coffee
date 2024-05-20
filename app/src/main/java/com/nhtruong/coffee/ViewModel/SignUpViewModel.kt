package com.nhtruong.coffee.ViewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nhtruong.coffee.model.UserModel

class SignUpViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("Users")

    fun registerUser(name: String, email: String, password: String, confirmPassword: String, action: (Boolean, String) -> Unit) {
        if (password != confirmPassword) {
            action(false, "Passwords do not match")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    firebaseUser?.let { user ->
                        val userId = user.uid
                        val newUserModel = UserModel(userId, name, email, "", "", password, "User") // Lưu ý về mật khẩu
                        dbRef.child(userId).setValue(newUserModel)
                            .addOnSuccessListener {
                                action(true, "Registered successfully")
                            }
                            .addOnFailureListener { e ->
                                action(false, e.message ?: "Failed to save user data.")
                            }
                    }
                } else {
                    action(false, task.exception?.message ?: "Registration failed.")
                }
            }
    }
}