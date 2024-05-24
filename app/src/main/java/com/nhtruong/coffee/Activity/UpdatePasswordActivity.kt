package com.nhtruong.coffee.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.nhtruong.coffee.R
import com.nhtruong.coffee.ViewModel.UpdatePasswordViewModel
import com.nhtruong.coffee.databinding.ActivityUpdatePasswordBinding


class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePasswordBinding
    private lateinit var viewModel: UpdatePasswordViewModel

    private var isPasswordVisible = false

    //chuc nang cap nhat mat khau 20/05/2024
    private var isObservingPasswordUpdated = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(UpdatePasswordViewModel::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.terra_red)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.peach)
        }
        binding.ivBack.setOnClickListener {

            finish()
        }
        // Set onClickListener for password visibility icon
        // Set onTouchListener for password visibility icon
        // Set onClickListener for drawableEnd of etPassword
        // Set onClickListener for password visibility icon
        binding.etPassword.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.etPassword.right - binding.etPassword.compoundDrawables[2].bounds.width())) {
                    // User clicked on drawableEnd (password visibility icon)
                    togglePasswordVisibility(binding.etPassword)
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Set onClickListener for confirm password visibility icon
        binding.etComfirmPassword.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.etComfirmPassword.right - binding.etComfirmPassword.compoundDrawables[2].bounds.width())) {
                    // User clicked on drawableEnd (password visibility icon)
                    togglePasswordVisibility(binding.etComfirmPassword)
                    return@setOnTouchListener true
                }
            }
            false
        }


        binding.btnChangePassword.setOnClickListener {
            val newPassword = binding.etPassword.text.toString()
            val confirmPassword = binding.etComfirmPassword.text.toString()

            if (newPassword == confirmPassword) {
                viewModel.updatePassword(newPassword){ message ->
                    showToast(message)
                }

            } else {
                showToast("Passwords do not match")
            }
        }


        //thay doi ngay 20/05/2024
        if (!isObservingPasswordUpdated) {
            viewModel.passwordUpdated.observe(this, { passwordUpdated ->
                if (passwordUpdated) {
                    showToast("Password updated")
                    finish()
                }
            })
            isObservingPasswordUpdated = true
        }

    }

    // Function to toggle password visibility
    private fun togglePasswordVisibility(editText: EditText) {
        if (isPasswordVisible) {
            // Password is currently visible, so hide it
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            editText.setSelection(editText.text.length) // Keep cursor at the end of text
            isPasswordVisible = false
        } else {
            // Password is currently hidden, so show it
            editText.transformationMethod = null
            editText.setSelection(editText.text.length) // Keep cursor at the end of text
            isPasswordVisible = true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



}










