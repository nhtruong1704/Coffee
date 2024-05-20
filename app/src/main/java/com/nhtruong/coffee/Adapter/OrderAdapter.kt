package com.nhtruong.coffee.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nhtruong.coffee.Activity.UserOrderDetailActivity
import com.nhtruong.coffee.databinding.ViewholderOrderBinding
import com.nhtruong.coffee.model.OrderModel


class OrderAdapter (private val orders: List<OrderModel>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>(){

    inner class OrderViewHolder(private val binding: ViewholderOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderModel) {
            binding.tvOrderDate.text = order.orderDate
            binding.tvOrderStatus.text = order.orderStatus
            binding.tvOrderPrice.text = "₽ ${order.orderTotal.toInt()}"
            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, UserOrderDetailActivity::class.java) // Fixed line
                intent.putExtra("orderId", order.orderId)
                itemView.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ViewholderOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount() = orders.size


}