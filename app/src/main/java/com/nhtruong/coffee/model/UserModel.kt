package com.nhtruong.coffee.model

data class UserModel(
    var userId: String ="",
    var name: String ="",
    var email: String ="",
    var phone: String ="",
    var imageUrl: String = "",
    var password: String = "",
    var userType: String ="User"
)
