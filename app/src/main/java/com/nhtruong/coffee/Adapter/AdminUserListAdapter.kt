package com.nhtruong.coffee.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nhtruong.coffee.R
import com.nhtruong.coffee.databinding.ViewholderAdminUserListBinding
import com.nhtruong.coffee.model.UserModel

class AdminUserListAdapter(private var userList: List<UserModel>) :
    RecyclerView.Adapter<AdminUserListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewholderAdminUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserModel) {
            binding.tvUserName.text = user.name
            binding.tvUserEmail.text = user.email
            binding.tvUserPhone.text = user.phone
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.user_1)
                .error(R.drawable.user_1)

            Glide.with(binding.ivUserPic.context)
                .load(user.imageUrl)
                .apply(requestOptions)
                .into(binding.ivUserPic)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderAdminUserListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount() = userList.size


    fun updateUserList(newUserList: List<UserModel>) {
        userList = newUserList
        notifyDataSetChanged()
    }
}
