package com.nhtruong.coffee.ViewModel

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nhtruong.coffee.model.UserModel

class SignInViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("Users")

    fun userSignIn(email: String, password: String, onComplete: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        dbRef.child(it).get().addOnSuccessListener { dataSnapshot ->
                            val user = dataSnapshot.getValue(UserModel::class.java)
                            user?.let { currentUser ->
                                onComplete(currentUser.userType)
                            } ?: onComplete(null) // UserModel not found in database
                        }.addOnFailureListener { e ->
                            onComplete(null) // Error getting user data from database
                        }
                    } ?: onComplete(null) // UserModel ID not found
                } else {
                    onComplete(null) // Sign in failed
                }
            }
    }






}
