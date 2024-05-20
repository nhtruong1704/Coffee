package com.nhtruong.coffee.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.nhtruong.coffee.ViewModel.UserProfileViewModel
import com.nhtruong.coffee.databinding.ActivityUserProfileBinding  // Đảm bảo đường dẫn này phù hợp với package của bạn

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var viewModel: UserProfileViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        viewModel.getUserData { user ->
            binding.etName.setText(user.name)
            binding.etEmail.setText(user.email)
            binding.etPhone.setText(user.phone)
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            viewModel.updateUserData(name, email, phone)
            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()

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

    //thay doi ngay 20/05/2024
    private fun clearUserLoginState() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}
