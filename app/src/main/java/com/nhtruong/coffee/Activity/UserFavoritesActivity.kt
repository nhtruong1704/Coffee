package com.nhtruong.coffee.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nhtruong.coffee.Adapter.FavoriteAdapter
import com.nhtruong.coffee.model.FavoriteModel
import com.nhtruong.coffee.databinding.ActivityUserFavoritesBinding

class UserFavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFavoritesBinding
    private lateinit var adapter: FavoriteAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { startActivity(Intent(this@UserFavoritesActivity, MainActivity::class.java)) }
        adapter = FavoriteAdapter(mutableListOf())

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFavorites.adapter = adapter

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Favorites").child(firebaseAuth.currentUser?.uid ?: "")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val favorites = dataSnapshot.children.mapNotNull { it.getValue(FavoriteModel::class.java) }
                adapter.updateFavorites(favorites)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
}