package com.nhtruong.coffee.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nhtruong.coffee.databinding.ViewholderOrderDetailBinding
import com.nhtruong.coffee.model.ItemsModel

// OrderDetailAdapter.kt
class OrderDetailAdapter(private val items: List<ItemsModel>) : RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>() {

    inner class OrderDetailViewHolder(private val binding: ViewholderOrderDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsModel) {
            binding.tvTitle.text = item.title
            binding.tvQuantity.text = "x${item.numberInCart}"
            Glide.with(binding.root)
                .load(item.picUrl)
                .into(binding.ivOrderPic)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val binding = ViewholderOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}