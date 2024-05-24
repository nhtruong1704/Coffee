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
import com.nhtruong.coffee.Adapter.AdminOrderListAdapter
import com.nhtruong.coffee.R
import com.nhtruong.coffee.databinding.ActivityAdminOrderListBinding
import com.nhtruong.coffee.model.OrderModel
import com.nhtruong.coffee.model.UserModel

class AdminOrderListActivity : AppCompatActivity() {

    private lateinit var orderList: MutableList<OrderModel>
    private lateinit var orderAdapter: AdminOrderListAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityAdminOrderListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { startActivity(Intent(this@AdminOrderListActivity, AdminMainActivity::class.java)) }
        orderList = mutableListOf()
        orderAdapter = AdminOrderListAdapter(orderList)
        binding.cartView.layoutManager = LinearLayoutManager(this)
        binding.cartView.adapter = orderAdapter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.terra_red)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderList.clear()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(OrderModel::class.java)
                    if (order != null) {
                        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(order.userId)
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                val user = userSnapshot.getValue(UserModel::class.java)
                                if (user != null) {
                                    order.userName = user.name
                                    orderList.add(order)
                                    orderList.sortWith(compareByDescending { it.orderDate })
                                    orderAdapter.notifyDataSetChanged()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle possible errors.
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })

        setupSearch()
    }

    private fun setupSearch() {
        binding.edtSearchOrder.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    filterOrders(s.toString())
                }
            }
        })
    }

    private fun filterOrders(searchText: String) {
        val filteredList = if (searchText.isEmpty()) {
            orderList
        } else {
            orderList.filter { order ->
                order.userName.contains(searchText, ignoreCase = true) ||
                        order.orderTotal.toString().contains(searchText, ignoreCase = true) ||
                        order.orderDate.contains(searchText, ignoreCase = true) ||
                        order.orderStatus.contains(searchText, ignoreCase = true)
            }
        }
        orderAdapter.updateOrderList(filteredList)
    }
}
