package com.nhtruong.coffee.Activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nhtruong.coffee.ViewModel.SignInViewModel
import com.nhtruong.coffee.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSignIn.setOnClickListener {
            val email = binding.edtEmailSignIn.text.toString().trim()
            val password = binding.edtPasswordSignIn.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.userSignIn(email, password) { userType ->
                    if (userType == "User") {
                        saveUserLoginState("User") // Lưu trạng thái đăng nhập
                        // Navigate to user main activity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (userType == "Admin") {
                        saveUserLoginState("Admin") // Lưu trạng thái đăng nhập
                        // Navigate to admin main activity
                        val intent = Intent(this, AdminMainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Handle invalid user type or other errors
                        Toast.makeText(this, "Invalid user type", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        //thay doi ngay 20/05/2024
        //chuc nang quen mat khau
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        //thay doi ngay 20/05/2024
        checkLoginState()

    }

    //thay doi ngay 20/05/2024
    //kiem tra trang thai dang nhap
    private fun saveUserLoginState(userType: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_logged_in", true)
        editor.putString("user_type", userType)
        editor.apply()
    }




    //chuc nang kiem tra dang nhap
    private fun checkLoginState() {

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        val userType = sharedPreferences.getString("user_type", "")
        if (isLoggedIn && userType != null) {
            val intent = when (userType) {
                "Admin" -> Intent(this, AdminMainActivity::class.java)
                "User" -> Intent(this, MainActivity::class.java)
                else -> null
            }
            if (intent != null) {
                startActivity(intent)
                finish()
            } else {
                // Handle case when userType is not "Admin" or "User"

            }
        } else {
            // Handle case when isLoggedIn is false or userType is null or empty

        }
    }


}



