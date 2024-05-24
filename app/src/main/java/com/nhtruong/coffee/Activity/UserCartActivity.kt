package com.nhtruong.coffee.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nhtruong.coffee.Helper.ManagmentCart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nhtruong.coffee.Adapter.CartAdapter
import com.nhtruong.coffee.Helper.ChangeNumberItemsListener
import com.nhtruong.coffee.R
import com.nhtruong.coffee.databinding.ActivityCartBinding
import com.nhtruong.coffee.model.OrderModel
import java.text.SimpleDateFormat
import java.util.*

class UserCartActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        managmentCart = ManagmentCart(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.terra_red)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        }

        initCartList()
        calculateCart()

        binding.btnBack.setOnClickListener { startActivity(Intent(this@UserCartActivity, MainActivity::class.java)) }

        binding.btnOrder.setOnClickListener {
            placeOrder()
        }




    }

    private fun placeOrder() {
        val deliveryFee = 100.0
        val orderId = FirebaseDatabase.getInstance().reference.child("Orders").push().key ?: ""
        val orderDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""  // Lấy từ đăng nhập hoặc session người dùng
        val orderTotal = managmentCart.getTotalFee() + deliveryFee // Giả sử làm tròn tổng số tiền

        val order = OrderModel(
            orderId = orderId,
            userId = userId,
            orderDate = orderDate,
            orderStatus = "Pending",
            orderTotal = orderTotal,
            orderItems = managmentCart.getListCart(userId)
        )

        FirebaseDatabase.getInstance().reference
            .child("Orders")
            .child(orderId)
            .setValue(order)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                    // Xử lý sau khi đặt hàng thành công, ví dụ như chuyển đến trang cảm ơn hoặc trạng thái đơn hàng
                    managmentCart.clearCart()
                    // Navigate back to MainActivity
                    startActivity(Intent(this@UserCartActivity, MainActivity::class.java))

                } else {
                    Toast.makeText(this, "Failed to place order.", Toast.LENGTH_SHORT).show()
                }
            }
    }
















    private fun calculateCart() {
        val delivery = 100
        val total=Math.round((managmentCart.getTotalFee()+delivery)*100)/100
        val itemTotal = Math.round(managmentCart.getTotalFee()*100)/100
        with(binding){
            tvSubtotal.text="₽ $itemTotal"
            tvDelivery.text="₽ $delivery"
            tvTotal.text="₽ ${total}"
        }
    }



    private fun initCartList() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        binding.cartView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.cartView.adapter=CartAdapter(managmentCart.getListCart(userId),this,object:ChangeNumberItemsListener{
            override fun onChanged() {
                calculateCart()
            }
        })
    }





}