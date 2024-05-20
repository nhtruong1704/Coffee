//package com.nhtruong.coffee.Helper
//
//import android.content.Context
//import android.widget.Toast
//import com.nhtruong.coffee.Helper.ChangeNumberItemsListener
//import com.nhtruong.coffee.Helper.TinyDB
//import com.nhtruong.coffee.model.ItemsModel
//
//
//class ManagmentCart(val context: Context) {
//
//    private val tinyDB = TinyDB(context)
//
//    fun insertItems(item: ItemsModel) {
//        var listDrink = getListCart()
//        val existAlready = listDrink.any { it.title == item.title }
//        val index = listDrink.indexOfFirst { it.title == item.title }
//
//        if (existAlready) {
//            listDrink[index].numberInCart = item.numberInCart
//        } else {
//            listDrink.add(item)
//        }
//        tinyDB.putListObject("CartList", listDrink)
//        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
//    }
//
//    fun getListCart(): ArrayList<ItemsModel> {
//        return tinyDB.getListObject("CartList") ?: arrayListOf()
//    }
//
//    fun minusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
//        if (listItems[position].numberInCart == 1) {
//            listItems.removeAt(position)
//        } else {
//            listItems[position].numberInCart--
//        }
//        tinyDB.putListObject("CartList", listItems)
//        listener.onChanged()
//    }
//
//    fun plusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
//        listItems[position].numberInCart++
//        tinyDB.putListObject("CartList", listItems)
//        listener.onChanged()
//    }
//// doan co nay da sua fee += item.price * item.numberInCart
//    //100 la phi de livery
//    fun getTotalFee(): Double {
//        val listDrink = getListCart()
//        var fee = 0.0
//        for (item in listDrink) {
//            fee += item.priceWithTopping * item.numberInCart
//        }
//        return fee
//    }
//
//    fun clearCart() {
//        tinyDB.putListObject("CartList", arrayListOf<ItemsModel>())
//    }
//
//
//}
//
//




package com.nhtruong.coffee.Helper

import android.content.Context
import android.widget.Toast
import com.nhtruong.coffee.Helper.ChangeNumberItemsListener
import com.nhtruong.coffee.Helper.TinyDB
import com.nhtruong.coffee.model.ItemsModel
import com.google.firebase.auth.FirebaseAuth

class ManagmentCart(val context: Context) {

    private val tinyDB = TinyDB(context)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun insertItems(item: ItemsModel) {
        val userId = auth.currentUser?.uid ?: return
        var listDrink = getListCart(userId)
        val existAlready = listDrink.any { it.title == item.title }
        val index = listDrink.indexOfFirst { it.title == item.title }

        if (existAlready) {
            listDrink[index].numberInCart = item.numberInCart
        } else {
            listDrink.add(item)
        }
        tinyDB.putListObject("CartList_$userId", listDrink)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(userId: String): ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList_$userId") ?: arrayListOf()
    }

    fun minusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        val userId = auth.currentUser?.uid ?: return
        if (listItems[position].numberInCart == 1) {
            listItems.removeAt(position)
        } else {
            listItems[position].numberInCart--
        }
        tinyDB.putListObject("CartList_$userId", listItems)
        listener.onChanged()
    }

    fun plusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        val userId = auth.currentUser?.uid ?: return
        listItems[position].numberInCart++
        tinyDB.putListObject("CartList_$userId", listItems)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val userId = auth.currentUser?.uid ?: return 0.0
        val listDrink = getListCart(userId)
        var fee = 0.0
        for (item in listDrink) {
            fee += item.priceWithTopping * item.numberInCart
        }
        return fee
    }

    fun clearCart() {
        val userId = auth.currentUser?.uid ?: return
        tinyDB.putListObject("CartList_$userId", arrayListOf<ItemsModel>())
    }
}




