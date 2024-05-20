package com.nhtruong.coffee.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nhtruong.coffee.model.ItemsModel

class DetailViewModel : ViewModel() {
    private var basePrice = 0.0
    private var sizeAdjustment = 0.0
    private var currentSize: String? = null
    private var toppingAdjustment = 0.0

    val totalPrice = MutableLiveData<Double>()
    private var _quantity = MutableLiveData<Int>(1)  // Sử dụng LiveData để quản lý số lượng
    val quantity: LiveData<Int> = _quantity  // Public LiveData for observation


    //moi them vao
    val priceWithSizeAndTopping = MutableLiveData<Double>()

    fun setBasePrice(basePrice: Double) {
        this.basePrice = basePrice
        updatePrice()
    }

    fun onSizeSelected(size: String, isSelected: Boolean) {
        val adjustment = when (size) {
            "M" -> 50.0
            "L" -> 100.0
            "S" -> 0.0   // No price adjustment for size S
            else -> 0.0
        }

        if (isSelected) {
            currentSize = size
            sizeAdjustment = adjustment
        } else if (size == currentSize) {
            currentSize = null
            sizeAdjustment = 0.0
        }
        updatePrice()
    }

    fun onToppingSelected(topping: String, isChecked: Boolean) {
        val adjustment = if (isChecked) 50.0 else -50.0
        toppingAdjustment += adjustment
        updatePrice()
    }


    fun updateQuantity(change: Int) {
        val newQuantity = (_quantity.value ?: 1) + change
        _quantity.value = if (newQuantity < 1) 1 else newQuantity  // Ensure quantity never goes below 1
        updatePrice()
    }


    private fun updatePrice() {
//        val priceWithSizeAndTopping = (basePrice + sizeAdjustment + toppingAdjustment)
//        val calculatedPrice = priceWithSizeAndTopping * (_quantity.value ?: 1)
//        totalPrice.value = calculatedPrice


        val priceWithSizeAndToppingValue = basePrice + sizeAdjustment + toppingAdjustment
        priceWithSizeAndTopping.value = priceWithSizeAndToppingValue
        totalPrice.value = priceWithSizeAndToppingValue * (_quantity.value ?: 1)


    }
}
