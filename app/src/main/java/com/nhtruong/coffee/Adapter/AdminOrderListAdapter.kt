package com.nhtruong.coffee.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nhtruong.coffee.R
import com.nhtruong.coffee.databinding.ViewholderAdminOrderListBinding
import com.nhtruong.coffee.model.OrderModel
import com.nhtruong.coffee.model.UserModel

class AdminOrderListAdapter(private var orderList: List<OrderModel>) :
    RecyclerView.Adapter<AdminOrderListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ViewholderAdminOrderListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderModel) {
            binding.tvTotal.text = "₽ ${order.orderTotal.toInt()}"
            binding.tvDateAndTime.text = order.orderDate

            val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            databaseReference.child(order.userId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    if (user != null) {
                        binding.tvName.text = user.name

                        val requestOptions = RequestOptions()
                            .placeholder(R.drawable.user_1)
                            .error(R.drawable.user_1)

                        Glide.with(binding.ivUserPic.context)
                            .load(user.imageUrl)
                            .apply(requestOptions)
                            .into(binding.ivUserPic)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderAdminOrderListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount() = orderList.size


    // Trong AdminOrderListAdapter.kt

    // Thêm phương thức updateOrderList
    fun updateOrderList(newOrderList: List<OrderModel>) {
        orderList = newOrderList
        notifyDataSetChanged()
    }

}