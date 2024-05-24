package com.nhtruong.coffee.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.nhtruong.coffee.Adapter.AdminUserListAdapter
import com.nhtruong.coffee.R
import com.nhtruong.coffee.databinding.ActivityAdminUserListBinding
import com.nhtruong.coffee.model.UserModel

class AdminUserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUserListBinding
    private lateinit var database: DatabaseReference
    private val userList = mutableListOf<UserModel>()
    private lateinit var userAdapter: AdminUserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.terra_red)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        }
        setupRecyclerView()
        setupSearch()
        binding.btnBack.setOnClickListener { startActivity(Intent(this@AdminUserListActivity, AdminMainActivity::class.java)) }
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    if (user != null && user.userType == "User") {
                        userList.add(user)
                    }
                }
                userAdapter.updateUserList(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
            }
        })
    }

    private fun setupRecyclerView() {
        userAdapter = AdminUserListAdapter(userList)
        binding.cartView.layoutManager = LinearLayoutManager(this)
        binding.cartView.adapter = userAdapter
    }





    //chuc nang search
    private fun setupSearch() {
        binding.edtSearchUser.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    filterUsers(s.toString())
                }
            }
        })
    }

    private fun filterUsers(searchText: String) {
        val filteredList = if (searchText.isEmpty()) {
            userList
        } else {
            userList.filter {
                it.name.contains(searchText, ignoreCase = true) ||
                        it.email.contains(searchText, ignoreCase = true) ||
                        it.phone.contains(searchText, ignoreCase = true)
            }
        }
        userAdapter.updateUserList(filteredList)
    }
}
