package com.nhtruong.coffee.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nhtruong.coffee.R
import com.nhtruong.coffee.ViewModel.ForgotPasswordViewModel
import com.nhtruong.coffee.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.terra_red)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.peach)
        }
        binding.btnContinue.setOnClickListener {
            val email = binding.edtEmailForgotPassword.text.toString().trim()
            if (email.isNotEmpty()) {
                viewModel.checkEmailExists(email) { exists ->
                    if (exists) {
                        // Email exists, navigate to UpdatePasswordActivity
                        viewModel.sendPasswordResetEmail(email)
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Email does not exist, show a toast message
                        Toast.makeText(this, "Email does not exist", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener { startActivity(Intent(this@ForgotPasswordActivity, SignInActivity::class.java)) }
    }
}