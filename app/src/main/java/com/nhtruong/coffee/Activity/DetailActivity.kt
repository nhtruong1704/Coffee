package com.nhtruong.coffee.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.nhtruong.coffee.Helper.ManagmentCart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nhtruong.coffee.R
import com.nhtruong.coffee.ViewModel.DetailViewModel
import com.nhtruong.coffee.databinding.ActivityDetailBinding
import com.nhtruong.coffee.model.FavoriteModel
import com.nhtruong.coffee.model.ItemsModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailBinding
    private lateinit var item:ItemsModel
    private var numberOrder=1
    private lateinit var managmentCart: ManagmentCart


    //chuc nang chon topping
    private val viewModel: DetailViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        managmentCart= ManagmentCart(this)
        getBundle()

        val drinkId = intent.getStringExtra("drinkId")
        if (drinkId != null) {
            val itemsRef = FirebaseDatabase.getInstance().getReference("Items").child(drinkId)
            itemsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    item = dataSnapshot.getValue(ItemsModel::class.java) ?: ItemsModel()
                    // update UI with item
                    binding.tvTitle.text = item.title
                    binding.tvPrice.text="₽ ${item.price.toInt()}"
                    binding.tvDescription.text = item.description
                    binding.tvRating.text = item.rating.toString()
                    Glide.with(this@DetailActivity)
                        .load(item.picUrl)
                        .apply(RequestOptions().transform(CenterCrop()))
                        .into(binding.ivDrink)
                    viewModel.setBasePrice(item.price)
                    checkFavoriteStatus()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors properly
                }
            })
        }








        //chuc nang chon topping


        binding.rbSizeS.isChecked = true
        viewModel.onSizeSelected("S", true)
        viewModel.totalPrice.observe(this, Observer { newPrice ->
            // Định dạng giá trị giá cả để không hiển thị phần thập phân nếu là số nguyên
            val formattedPrice = if (newPrice % 1.0 == 0.0) {
                String.format("₽ %d", newPrice.toInt())
            } else {
                String.format("₽ %.2f", newPrice)
            }
            binding.btnAddToCart.text = formattedPrice
        })





        binding.ivFavorite.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val favoritesRef = FirebaseDatabase.getInstance().getReference("Favorites").child(user?.uid ?: "")
            val favoriteItem = FavoriteModel(item.drinkId,item.picUrl, item.title, item.price.toString())

            favoritesRef.orderByChild("picUrl").equalTo(item.picUrl).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var itemExists = false
                    dataSnapshot.children.forEach { snapshot ->
                        if (snapshot.child("title").value == item.title) {
                            snapshot.ref.removeValue()
                            binding.ivFavorite.setImageResource(R.drawable.fav_icon)
                            Toast.makeText(this@DetailActivity, "Removed from favorites", Toast.LENGTH_SHORT).show()
                            itemExists = true
                            return@forEach
                        }
                    }
                    if (!itemExists) {
                        favoritesRef.push().setValue(favoriteItem)
                        binding.ivFavorite.setImageResource(R.drawable.fav_icon_red)
                        Toast.makeText(this@DetailActivity, "Added to favorites", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@DetailActivity, "Failed to update favorites: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }


        setupSizeAndToppingListeners()
        setupQuantityControls()



        checkFavoriteStatus()

    }

    private fun getBundle(){
        item = intent.getParcelableExtra<ItemsModel>("object") ?: ItemsModel()
        binding.tvTitle.text=item.title
        binding.tvDescription.text=item.description
        binding.tvPrice.text="₽ ${item.price.toInt()}"
        binding.tvRating.text="${item.rating}"
        binding.ivDrink

        //moi them doan code nay
        viewModel.quantity.observe(this, Observer { quantity ->
            item.numberInCart = quantity // Update the numberInCart with the current quantity
            binding.tvQuantity.text = quantity.toString()
        })

        viewModel.priceWithSizeAndTopping.observe(this, Observer { priceWithToppings ->
            item.priceWithTopping = priceWithToppings  // Update the price with topping
        })



        binding.btnAddToCart.setOnClickListener{
           // item.numberInCart = quantity
            managmentCart.insertItems(item)

           startActivity(Intent(this@DetailActivity,UserCartActivity::class.java))


        }







        viewModel.setBasePrice(item.price)

        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(this)
            .load(item.picUrl)
            .apply(requestOptions)
            .into(binding.ivDrink)

        binding.btnBack.setOnClickListener{
            finish()
        }


    }

    //chuc nang them topping
    private fun setupSizeAndToppingListeners() {
        binding.rgSize.setOnCheckedChangeListener { _, checkedId ->
            val isChecked = binding.rgSize.findViewById<RadioButton>(checkedId).isChecked
            when (checkedId) {
                R.id.rbSizeS -> viewModel.onSizeSelected("S", isChecked)
                R.id.rbSizeM -> viewModel.onSizeSelected("M", isChecked)
                R.id.rbSizeL -> viewModel.onSizeSelected("L", isChecked)
            }
        }

        listOf(binding.cbCreemCheese, binding.cbCoffeeJelly, binding.cbWhiteBubbles).forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.onToppingSelected(buttonView.text.toString(), isChecked)
            }
        }
    }



    //chuc nang tang giam so luong
    private fun setupQuantityControls() {
        viewModel.quantity.observe(this, Observer { quantity ->
            binding.tvQuantity.text = quantity.toString()
        })

        binding.btnIncrease.setOnClickListener {
            viewModel.updateQuantity(1)
        }

        binding.btnDecrease.setOnClickListener {
            viewModel.updateQuantity(-1)
        }
    }


    private fun checkFavoriteStatus() {
        val user = FirebaseAuth.getInstance().currentUser
        val favoritesRef = FirebaseDatabase.getInstance().getReference("Favorites").child(user?.uid ?: "")
        favoritesRef.orderByChild("drinkId").equalTo(item.drinkId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isFavorite = false
                dataSnapshot.children.forEach { snapshot ->
                    if (snapshot.child("drinkId").value == item.drinkId) {
                        isFavorite = true
                        return@forEach
                    }
                }
                binding.ivFavorite.setImageResource(if (isFavorite) R.drawable.fav_icon_red else R.drawable.fav_icon)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors properly
            }
        })
    }



}