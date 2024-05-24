package com.nhtruong.coffee.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.nhtruong.coffee.Adapter.OrderDetailAdapter
import com.nhtruong.coffee.R
import com.nhtruong.coffee.model.OrderModel
import com.nhtruong.coffee.databinding.ActivityUserOrderDetailBinding

class UserOrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserOrderDetailBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { startActivity(Intent(this@UserOrderDetailActivity, UserOrderActivity::class.java)) }
        val orderId = intent.getStringExtra("orderId") ?: return
        database = FirebaseDatabase.getInstance().getReference("Orders").child(orderId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.terra_red)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        }
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val order = snapshot.getValue(OrderModel::class.java) ?: return
                binding.tvOrderDate.text = order.orderDate
                binding.tvOrderStatus.text = order.orderStatus
                binding.tvTotal.text = "₽ ${order.orderTotal.toInt()}"

                binding.cartView.layoutManager = LinearLayoutManager(this@UserOrderDetailActivity)
                binding.cartView.adapter = OrderDetailAdapter(order.orderItems)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
            }
        })
    }
}