//package com.nhtruong.coffee.Adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.nhtruong.coffee.databinding.ViewholderDrinkBinding
//import com.nhtruong.coffee.model.ItemsModel
//
//class AdminMainAdapter(private var itemsList: List<ItemsModel>) : RecyclerView.Adapter<AdminMainAdapter.ViewHolder>() {
//
//    inner class ViewHolder(private val binding: ViewholderDrinkBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: ItemsModel) {
//            binding.tvTitle.text = item.title
//            binding.tvPrice.text = "₽ ${item.price.toInt()}"
//            Glide.with(binding.ivPic.context).load(item.picUrl).into(binding.ivPic)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ViewholderDrinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(itemsList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return itemsList.size
//    }
//
//    fun updateItemList(newItemList: List<ItemsModel>) {
//        itemsList = newItemList
//        notifyDataSetChanged()
//    }
//
//
//
//
//
//}







package com.nhtruong.coffee.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nhtruong.coffee.databinding.ViewholderDrinkBinding
import com.nhtruong.coffee.model.ItemsModel

class AdminMainAdapter(
    private var itemsList: List<ItemsModel>,
    private val onItemClick: (ItemsModel) -> Unit
) : RecyclerView.Adapter<AdminMainAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewholderDrinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsModel) {
            binding.tvTitle.text = item.title
            binding.tvPrice.text = "₽ ${item.price.toInt()}"
            Glide.with(binding.ivPic.context).load(item.picUrl).into(binding.ivPic)
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderDrinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemsList[position])
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateItemList(newItemList: List<ItemsModel>) {
        itemsList = newItemList
        notifyDataSetChanged()
    }
}

