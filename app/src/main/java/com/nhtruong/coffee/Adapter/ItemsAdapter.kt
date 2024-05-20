package com.nhtruong.coffee.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nhtruong.coffee.Activity.DetailActivity
import com.nhtruong.coffee.databinding.ViewholderDrinkBinding
import com.nhtruong.coffee.model.ItemsModel

class ItemsAdapter(val items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    private lateinit var context: Context

    inner class ViewHolder(val binding: ViewholderDrinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    val intent = Intent(context, DetailActivity::class.java).apply {
                        putExtra("drinkId", item.drinkId)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapter.ViewHolder {
        context = parent.context
        val binding =
            ViewholderDrinkBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvPrice.text = item.price.toInt().toString()
        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .into(holder.binding.ivPic)
    }

    override fun getItemCount(): Int = items.size
}
