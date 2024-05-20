package com.nhtruong.coffee.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import com.nhtruong.coffee.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //chuc nang kiem tra dang nhap
        checkLoginState()


        binding.btnStart.setOnClickListener{
            val intent = Intent(this@IntroActivity, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

//            startActivity(Intent(this@IntroActivity, SignInActivity::class.java))
        }
        binding.tvSignUp.setOnClickListener{
            startActivity(Intent(this@IntroActivity, SignUpActivity::class.java))
        }
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