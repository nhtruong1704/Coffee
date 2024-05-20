package com.nhtruong.coffee.model

data class OrderModel(
    var orderId: String = "",
    var userId: String = "",
    var orderDate: String = "",
    var orderStatus: String = "",
    var orderTotal: Double = 0.0,
    var orderItems: ArrayList<ItemsModel> = ArrayList()
)
