package com.nhtruong.coffee.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


import com.nhtruong.coffee.databinding.ActivityAdminMainBinding


class AdminMainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityAdminMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }




    private fun setupNavigation() {


        binding.ivDrinks.setOnClickListener {
            // Handle click event for "Drinks" button
            // For example, navigate to DrinksActivity
            val intent = Intent(this, AdminMainActivity::class.java)
            startActivity(intent)
        }

//        binding.ivUsers.setOnClickListener {
//            // Handle click event for "Users" button
//            // For example, navigate to UsersActivity
//            val intent = Intent(this, UsersActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.ivOrders.setOnClickListener {
//            // Handle click event for "Orders" button
//            // For example, navigate to OrdersActivity
//            val intent = Intent(this, OrdersActivity::class.java)
//            startActivity(intent)
//        }

        binding.ivProfile.setOnClickListener {
            // Handle click event for "Profile" button
            // For example, navigate to ProfileActivity
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }


    }
}