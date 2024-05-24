//package com.nhtruong.coffee.Activity
//
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat.startActivity
//import androidx.databinding.DataBindingUtil.setContentView
//import androidx.lifecycle.ViewModelProvider
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference
//import com.nhtruong.coffee.ViewModel.AdminProfileViewModel
//import com.nhtruong.coffee.databinding.ActivityAdminProfileBinding
//
//
//class AdminProfileActivity : AppCompatActivity(){
//    private lateinit var binding: ActivityAdminProfileBinding
//    private lateinit var viewModel: AdminProfileViewModel
//
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAdminProfileBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        viewModel = ViewModelProvider(this).get(AdminProfileViewModel::class.java)
//
//        viewModel.getUserData { user ->
//            binding.etName.setText(user.name)
//            binding.etEmail.setText(user.email)
//            binding.etPhone.setText(user.phone)
//        }
//
//        binding.btnSave.setOnClickListener {
//            val name = binding.etName.text.toString()
//            val email = binding.etEmail.text.toString()
//            val phone = binding.etPhone.text.toString()
//            viewModel.updateUserData(name, email, phone)
//            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()
//
//        }
//        // Navigation to MainActivity on back button click
//        binding.ivBack.setOnClickListener {
//            val intent = Intent(this, AdminMainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        // Sign out button click listener
//        binding.btnSignOut.setOnClickListener {
//
//            //thay doi ngay 20/05/2024
//            clearUserLoginState()
//
//            FirebaseAuth.getInstance().signOut()
//            val intent = Intent(this, IntroActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
//        }
//
//        // Navigate to UserUpdatePassword Activity on update password button click
//        binding.btnUpdatePassword.setOnClickListener {
//            val intent = Intent(this, UpdatePasswordActivity::class.java)
//            startActivity(intent)
//        }
//
//
//
//
//
//
//    }
//
//    //thay doi ngay 20/05/2024
//    private fun clearUserLoginState() {
//        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.clear()
//        editor.apply()
//    }
//}

















package com.nhtruong.coffee.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nhtruong.coffee.R
import com.nhtruong.coffee.ViewModel.AdminProfileViewModel
import com.nhtruong.coffee.databinding.ActivityAdminProfileBinding
import java.util.*

class AdminProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminProfileBinding
    private lateinit var viewModel: AdminProfileViewModel
    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference
    private val defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/coffee-16fc2.appspot.com/o/user_1.png?alt=media&token=05db05ae-54b4-4947-86d3-d761fee34208"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AdminProfileViewModel::class.java)
        storageReference = FirebaseStorage.getInstance().reference

        viewModel.getUserData { user ->
            binding.etName.setText(user.name)
            binding.etEmail.setText(user.email)
            binding.etPhone.setText(user.phone)
            if (user.imageUrl.isNotEmpty()) {
                // Load image using Glide
                Glide.with(this).load(user.imageUrl).into(binding.ivAvatar)
            } else {
                binding.ivAvatar.setImageResource(R.drawable.user_1)
            }
        }

        val getImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                binding.ivAvatar.setImageURI(imageUri)
            }
        }

        binding.tvChangeAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getImage.launch(intent)
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()

            if (imageUri != null) {
                uploadImageToFirebaseStorage(imageUri!!) { imageUrl ->
                    viewModel.updateUserData(name, email, phone, imageUrl)
                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()
                }
            } else {
                viewModel.updateUserData(name, email, phone, defaultImageUrl)
                Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivBack.setOnClickListener {
            val intent = Intent(this, AdminMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignOut.setOnClickListener {
            clearUserLoginState()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnUpdatePassword.setOnClickListener {
            val intent = Intent(this, UpdatePasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri, onComplete: (String) -> Unit) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = storageReference.child("images/$fileName")

        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    onComplete(uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearUserLoginState() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}


