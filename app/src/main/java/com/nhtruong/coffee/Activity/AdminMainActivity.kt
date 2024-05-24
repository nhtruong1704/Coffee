//package com.nhtruong.coffee.Activity
//
//import android.content.Intent
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.firebase.database.*
//import com.nhtruong.coffee.Adapter.AdminMainAdapter
//
//
//import com.nhtruong.coffee.databinding.ActivityAdminMainBinding
//import com.nhtruong.coffee.model.ItemsModel
//
//
//class AdminMainActivity : AppCompatActivity(){
//    private lateinit var binding: ActivityAdminMainBinding
//    private lateinit var database: DatabaseReference
//    private lateinit var itemList: ArrayList<ItemsModel>
//    private lateinit var adapter: AdminMainAdapter
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAdminMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setupNavigation()
//        setupRecyclerView()
//        fetchDataFromFirebase()
//        setupSearch()
//    }
//
//    private fun setupRecyclerView() {
//        itemList = ArrayList()
//        adapter = AdminMainAdapter(itemList)
//        binding.cartView.layoutManager = LinearLayoutManager(this)
//        binding.cartView.adapter = adapter
//    }
//
//    private fun fetchDataFromFirebase() {
//        database = FirebaseDatabase.getInstance().getReference("Items")
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                itemList.clear()
//                for (dataSnapshot in snapshot.children) {
//                    val item = dataSnapshot.getValue(ItemsModel::class.java)
//                    item?.let { itemList.add(it) }
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle database error
//            }
//        })
//    }
//
//
//    private fun setupNavigation() {
//
//
//        binding.ivDrinks.setOnClickListener {
//
//            val intent = Intent(this, AdminMainActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.ivUsers.setOnClickListener {
//
//            val intent = Intent(this, AdminUserListActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.ivOrders.setOnClickListener {
//
//            val intent = Intent(this, AdminOrderListActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.ivProfile.setOnClickListener {
//
//            val intent = Intent(this, AdminProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.fabAddDrink.setOnClickListener {
//            val intent = Intent(this, AdminAddDrinkActivity::class.java)
//            startActivity(intent)
//        }
//
//    }
//
//
//    private fun setupSearch() {
//        binding.edtSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Do nothing
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Do nothing
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if (s != null) {
//                    filterItems(s.toString())
//                }
//            }
//        })
//    }
//
//    private fun filterItems(searchText: String) {
//        val filteredList = if (searchText.isEmpty()) {
//            itemList
//        } else {
//            itemList.filter { item ->
//                item.title.contains(searchText, ignoreCase = true) ||
//                        item.price.toString().contains(searchText, ignoreCase = true)
//            }
//        }
//        adapter.updateItemList(filteredList)
//    }
//
//
//
//
//
//
//
//
//
//}












package com.nhtruong.coffee.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.nhtruong.coffee.Adapter.AdminMainAdapter
import com.nhtruong.coffee.databinding.ActivityAdminMainBinding
import com.nhtruong.coffee.model.ItemsModel

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var itemList: ArrayList<ItemsModel>
    private lateinit var adapter: AdminMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupRecyclerView()
        fetchDataFromFirebase()
        setupSearch()
    }

    private fun setupRecyclerView() {
        itemList = ArrayList()
        adapter = AdminMainAdapter(itemList) { item ->
            val intent = Intent(this, AdminUpdateDrinkActivity::class.java)
            intent.putExtra("drinkId", item.drinkId)
            startActivity(intent)
        }
        binding.cartView.layoutManager = LinearLayoutManager(this)
        binding.cartView.adapter = adapter
    }

    private fun fetchDataFromFirebase() {
        database = FirebaseDatabase.getInstance().getReference("Items")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(ItemsModel::class.java)
                    item?.let { itemList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun setupNavigation() {
        binding.ivDrinks.setOnClickListener {
            val intent = Intent(this, AdminMainActivity::class.java)
            startActivity(intent)
        }

        binding.ivUsers.setOnClickListener {
            val intent = Intent(this, AdminUserListActivity::class.java)
            startActivity(intent)
        }

        binding.ivOrders.setOnClickListener {
            val intent = Intent(this, AdminOrderListActivity::class.java)
            startActivity(intent)
        }

        binding.ivProfile.setOnClickListener {
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }

        binding.fabAddDrink.setOnClickListener {
            val intent = Intent(this, AdminAddDrinkActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupSearch() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    filterItems(s.toString())
                }
            }
        })
    }

    private fun filterItems(searchText: String) {
        val filteredList = if (searchText.isEmpty()) {
            itemList
        } else {
            itemList.filter { item ->
                item.title.contains(searchText, ignoreCase = true) ||
                        item.price.toString().contains(searchText, ignoreCase = true)
            }
        }
        adapter.updateItemList(filteredList)
    }
}
