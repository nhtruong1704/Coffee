package com.nhtruong.coffee.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nhtruong.coffee.Activity.DrinkListActivity
import com.nhtruong.coffee.databinding.ViewholderCategoryBinding
import com.nhtruong.coffee.model.CategoryModel

class CategoryAdapter(val items: MutableList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.Viewholder>() {
    private lateinit var context: Context

    inner class Viewholder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = items[position]
                    val intent = Intent(context, DrinkListActivity::class.java)
                    intent.putExtra("categoryTitle", clickedItem.title)
                    context.startActivity(intent)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item=items[position]
        holder.binding.titleDrink.text=item.title
        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .into(holder.binding.picDrink)
    }

    override fun getItemCount(): Int = items.size




}