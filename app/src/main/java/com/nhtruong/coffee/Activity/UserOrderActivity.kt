package com.nhtruong.coffee.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nhtruong.coffee.Adapter.OrderAdapter
import com.nhtruong.coffee.R
import com.nhtruong.coffee.databinding.ActivityOrderBinding
import com.nhtruong.coffee.model.OrderModel

class UserOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var database: DatabaseReference
    private val orders = mutableListOf<OrderModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { startActivity(Intent(this@UserOrderActivity, MainActivity::class.java)) }
        database = FirebaseDatabase.getInstance().getReference("Orders")
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        database.orderByChild("userId").equalTo(userId).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orders.clear()
                for (dataSnapshot in snapshot.children) {
                    val order = dataSnapshot.getValue(OrderModel::class.java)
                    if (order != null) {
                        orders.add(order)
                    }
                }
                orders.sortWith(compareByDescending { it.orderDate })
                binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this@UserOrderActivity)
                binding.recyclerViewOrders.adapter = OrderAdapter(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
            }
        })
    }
}