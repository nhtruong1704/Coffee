package com.nhtruong.coffee.Activity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nhtruong.coffee.R
import com.nhtruong.coffee.ViewModel.UserProfileViewModel
import com.nhtruong.coffee.databinding.ActivityUserProfileBinding  // Đảm bảo đường dẫn này phù hợp với package của bạn
import java.util.*

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var viewModel: UserProfileViewModel
    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference
    private val defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/coffee-16fc2.appspot.com/o/user_3.png?alt=media&token=bcf6a574-0034-4ebb-84b0-cd01e0486ca3"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.terra_red)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.peach)
        }
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)
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
        // Navigation to MainActivity on back button click
        binding.ivBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Sign out button click listener
        binding.btnSignOut.setOnClickListener {

            //thay doi ngay 20/05/2024
            clearUserLoginState()

            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Navigate to UserUpdatePassword Activity on update password button click
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
    //thay doi ngay 20/05/2024
    private fun clearUserLoginState() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}
