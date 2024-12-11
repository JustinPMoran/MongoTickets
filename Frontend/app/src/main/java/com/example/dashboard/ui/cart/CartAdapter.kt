package com.example.dashboard.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboard.R
import dataClasses.CartItem

class CartAdapter(
    val items: MutableList<CartItem>,
    private val onRemoveFromCart: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = items[position]
        holder.sectionRow.text = "Section: ${cartItem.section}, Row: ${cartItem.row}"
        holder.ticketCost.text = "Cost: $${cartItem.ticketCost}"
        holder.ticketQuantity.text = "Qty: ${cartItem.ticketQuantity}"

        holder.removeButton.setOnClickListener {
            onRemoveFromCart(cartItem)
        }
    }

    override fun getItemCount() = items.size

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sectionRow: TextView = view.findViewById(R.id.section)
        val ticketCost: TextView = view.findViewById(R.id.ticketCost)
        val ticketQuantity: TextView = view.findViewById(R.id.ticketQuantity)
        val removeButton: Button = view.findViewById(R.id.removeItemButton)
    }
}
