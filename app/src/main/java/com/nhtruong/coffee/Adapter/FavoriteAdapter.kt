package com.nhtruong.coffee.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.nhtruong.coffee.Activity.DetailActivity
import com.nhtruong.coffee.R
import com.nhtruong.coffee.model.FavoriteModel
import com.nhtruong.coffee.databinding.ActivityUserFavoritesBinding
import com.nhtruong.coffee.databinding.ViewholderDrinkBinding
import com.nhtruong.coffee.model.ItemsModel

class FavoriteAdapter(private var favoriteItems: MutableList<FavoriteModel>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewholderDrinkBinding.bind(view)

        fun bind(favoriteItem: FavoriteModel) {
            Glide.with(itemView.context)
                .load(favoriteItem.picUrl)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(binding.ivPic)
            binding.tvTitle.text = favoriteItem.title
            binding.tvPrice.text = "₽ ${favoriteItem.price.toDoubleOrNull()?.toInt() ?: 0}"
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("drinkId", favoriteItems[position].drinkId)
                itemView.context.startActivity(intent)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_drink, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteItems[position])
    }

    override fun getItemCount(): Int {
        return favoriteItems.size
    }

    fun updateFavorites(favoriteItems: List<FavoriteModel>) {
        this.favoriteItems.clear()
        this.favoriteItems.addAll(favoriteItems)
        notifyDataSetChanged()
    }
}