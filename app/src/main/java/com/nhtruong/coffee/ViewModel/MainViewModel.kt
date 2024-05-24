package com.nhtruong.coffee.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nhtruong.coffee.Adapter.BestSellerAdapter
import com.nhtruong.coffee.model.CategoryModel
import com.nhtruong.coffee.model.ItemsModel
import com.nhtruong.coffee.model.SliderModel
import com.nhtruong.coffee.model.UserModel


class MainViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _category = MutableLiveData<MutableList<CategoryModel>>()
    private val _bestSeller = MutableLiveData<MutableList<ItemsModel>>()

    //Chuc Nang Hien Thi Ten Nguoi Dung
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName


    val banners: LiveData<List<SliderModel>> = _banner
    val category: LiveData<MutableList<CategoryModel>> = _category
    val bestSeller: LiveData<MutableList<ItemsModel>> = _bestSeller


    fun loadBanners() {
        val Ref = firebaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                    _banner.value = lists
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun loadCategory() {
        val Ref = firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(CategoryModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _category.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    fun loadBestSeller() {
        val Ref = firebaseDatabase.getReference("Items")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _bestSeller.value = lists
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun loadUserName(userId: String) {
        val userRef = firebaseDatabase.getReference("Users").child(userId)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _userName.value = user?.name
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainViewModel", "Failed to load user name", error.toException())
            }
        })
    }


    //chuc nang tim kiem 20/05/2024
    fun searchItems(searchText: String) {
        val searchQuery = firebaseDatabase.getReference("Items")
            .orderByChild("title")

        searchQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemsList = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(ItemsModel::class.java)
                    item?.let {
                        // Kiểm tra xem title của item có chứa searchText không
                        if (it.title.contains(searchText, ignoreCase = true)) {
                            itemsList.add(it)
                        }
                    }
                }
                _bestSeller.value = itemsList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainViewModel", "Error searching items: ${error.message}")
            }
        })
    }





}