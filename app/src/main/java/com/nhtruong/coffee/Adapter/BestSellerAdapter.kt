package com.nhtruong.coffee.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.nhtruong.coffee.Activity.DetailActivity
import com.nhtruong.coffee.databinding.ViewholderBestSellerBinding
import com.nhtruong.coffee.model.ItemsModel

class BestSellerAdapter(val items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<BestSellerAdapter.Viewholder>() {
    private var context: Context? = null

    class Viewholder(val binding: ViewholderBestSellerBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestSellerAdapter.Viewholder {
        context = parent.context
        val binding =
            ViewholderBestSellerBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: BestSellerAdapter.Viewholder, position: Int) {

        holder.binding.titleTxt.text =items[position].title
        holder.binding.priceTxt.text = "₽"+items[position].price.toInt().toString()
        holder.binding.ratingTxt.text = items[position].rating.toString()

        val requestOptions= RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(items[position].picUrl)
            .apply(requestOptions)
            .into(holder.binding.picBestSeller)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object", items[position])
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = items.size
}