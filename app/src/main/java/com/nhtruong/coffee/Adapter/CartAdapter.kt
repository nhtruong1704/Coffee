package com.nhtruong.coffee.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.nhtruong.coffee.Helper.ManagmentCart
import com.nhtruong.coffee.Helper.ChangeNumberItemsListener
import com.nhtruong.coffee.databinding.ViewholderCartBinding
import com.nhtruong.coffee.model.ItemsModel

class CartAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    context: Context,
    var changeNumberItemsListener: ChangeNumberItemsListener? = null

) : RecyclerView.Adapter<CartAdapter.ViewHolder> (){
    class ViewHolder(val binding: ViewholderCartBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    private val managmentCart = ManagmentCart(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding =
            ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val item = listItemSelected[position]

        holder.binding.tvTitle.text = item.title
        //holder.binding.tvFeeEachItem.text = "₽ ${item.price}"
        holder.binding.tvTotalEachItem.text = "₽ ${Math.round(item.numberInCart*item.priceWithTopping)}"
        holder.binding.tvNumberItem.text=item.numberInCart.toString()


        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.ivPicCart)







        holder.binding.btnPlusCart.setOnClickListener {
            managmentCart.plusItem(listItemSelected,position,object :ChangeNumberItemsListener{
                override fun onChanged(){
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }


        holder.binding.btnMinusCart.setOnClickListener {
            managmentCart.minusItem(listItemSelected,position,object :ChangeNumberItemsListener{
                override fun onChanged(){
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }




















    }

    override fun getItemCount(): Int = listItemSelected.size

}