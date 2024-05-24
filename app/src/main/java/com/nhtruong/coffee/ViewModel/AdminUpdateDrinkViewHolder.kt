package com.nhtruong.coffee.ViewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.nhtruong.coffee.model.ItemsModel

class AdminUpdateDrinkViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().getReference("Items")

    fun getDrinkData(drinkId: String, onComplete: (ItemsModel) -> Unit) {
        database.child(drinkId).get().addOnSuccessListener {
            val item = it.getValue(ItemsModel::class.java)
            item?.let { drink ->
                onComplete(drink)
            }
        }
    }

    fun updateDrink(drinkId: String, title: String, category: String, price: Double, description: String, imageUrl: String = "") {
        val drinkMap = mutableMapOf<String, Any>(
            "title" to title,
            "category" to category,
            "price" to price,
            "description" to description
        )
        if (imageUrl.isNotEmpty()) {
            drinkMap["picUrl"] = imageUrl
        }
        database.child(drinkId).updateChildren(drinkMap)
    }

    fun deleteDrink(drinkId: String, onComplete: () -> Unit) {
        database.child(drinkId).removeValue().addOnSuccessListener {
            onComplete()
        }
    }
}
