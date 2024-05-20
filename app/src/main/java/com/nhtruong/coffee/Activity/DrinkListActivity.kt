package com.nhtruong.coffee.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.nhtruong.coffee.Adapter.CategoryAdapter
import com.nhtruong.coffee.Adapter.ItemsAdapter
import com.nhtruong.coffee.databinding.ActivityDrinkListBinding
import com.nhtruong.coffee.model.CategoryModel
import com.nhtruong.coffee.model.ItemsModel

class DrinkListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrinkListBinding
    private lateinit var drinksAdapter: ItemsAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrinkListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryTitle = intent.getStringExtra("categoryTitle")
        if (categoryTitle != null) {
            binding.tvCatergoryName.text = categoryTitle
            databaseReference = FirebaseDatabase.getInstance().getReference("Items")
            databaseReference.orderByChild("category").equalTo(categoryTitle)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val drinks = dataSnapshot.children.mapNotNull {
                            it.getValue(ItemsModel::class.java)
                        }.toMutableList()
                        setupRecyclerView(drinks)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle possible errors properly
                    }
                })
        }

        binding.btnBack.setOnClickListener { startActivity(Intent(this@DrinkListActivity, MainActivity::class.java)) }

    }

    private fun setupRecyclerView(drinks: MutableList<ItemsModel>) {
        drinksAdapter = ItemsAdapter(drinks)
        binding.cartView.apply {
            layoutManager = LinearLayoutManager(this@DrinkListActivity)
            adapter = drinksAdapter
        }
    }
}
