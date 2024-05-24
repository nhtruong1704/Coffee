package com.nhtruong.coffee.ViewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.nhtruong.coffee.model.ItemsModel

class AdminAddDrinkViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().getReference("Items")

    // 2. Phương thức thêm đồ uống mới
    fun addDrink(title: String, category: String, price: Double, description: String, picUrl: String, onComplete: () -> Unit) {
        // Lấy danh sách hiện tại từ Firebase
        database.get().addOnSuccessListener { snapshot ->
            // Lấy số thứ tự tiếp theo
            val maxId = snapshot.children.mapNotNull { it.key?.toIntOrNull() }.maxOrNull() ?: -1
            val nextId = maxId + 1

            val drink = ItemsModel(
                drinkId = nextId.toString(),
                title = title,
                category = category,
                price = price,
                description = description,
                picUrl = picUrl
            )
            // Lưu dữ liệu vào Firebase
            database.child(nextId.toString()).setValue(drink).addOnCompleteListener {
                if (it.isSuccessful) {
                    onComplete()
                }
            }
        }
    }
}