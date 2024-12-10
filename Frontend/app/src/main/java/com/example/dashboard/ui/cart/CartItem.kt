package com.example.dashboard.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboard.R
import dataClasses.CartManager.cartItems

data class CartItem(val name: String, val price: Double, val quantity: Int)

class CartAdapter(private val items: List<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.eventName.text = cartItem.eventName
        holder.ticketCost.text = "Cost: $${cartItem.ticketCost}"
        holder.ticketQuantity.text = "Qty: ${cartItem.ticketQuantity}"
    }

    override fun getItemCount() = cartItems.size

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName: TextView = view.findViewById(R.id.cartItemName)
        val ticketCost: TextView = view.findViewById(R.id.cartItemPrice)
        val ticketQuantity: TextView = view.findViewById(R.id.cartItemQuantity)
    }
}
