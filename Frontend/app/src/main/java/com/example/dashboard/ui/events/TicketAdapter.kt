package com.example.dashboard.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboard.R
import com.example.dashboard.ui.cart.CartManager
import dataClasses.CartItem
import dataClasses.Ticket

class TicketAdapter(private var tickets: List<Ticket>) :
    RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.bind(ticket)
    }

    override fun getItemCount() = tickets.size

    fun updateTickets(newTickets: List<Ticket>) {
        tickets = newTickets
        notifyDataSetChanged()
    }

    inner class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ticketIdText: TextView = view.findViewById(R.id.ticketEventName)
        private val ticketDetailsText: TextView = view.findViewById(R.id.ticketDate)
        private val ticketCostText: TextView = view.findViewById(R.id.ticketCost)
        private val addToCartButton: Button = view.findViewById(R.id.addToCartButton)

        fun bind(ticket: Ticket) {
            val ticketId = ticket.id ?: -1
            val section = ticket.section ?: "Unknown"
            val row = ticket.row ?: "Unknown"
            val price = ticket.price ?: 0.0

            ticketIdText.text = "Ticket ID: $ticketId"
            ticketDetailsText.text = "Section: $section, Row: $row"
            ticketCostText.text = "Cost: $price USD"

            addToCartButton.setOnClickListener {
                val cartItem = CartItem(
                    ticketId = ticketId,
                    section = section,
                    row = row,
                    ticketCost = price,
                    ticketQuantity = 1
                )
                CartManager.addToCart(cartItem, 5) { success ->
                    if (success) {
                        addToCartButton.text = "Added to Cart"
                        addToCartButton.isEnabled = false
                        Toast.makeText(itemView.context, "Item added to cart", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(itemView.context, "Item not added to cart", Toast.LENGTH_SHORT).show()
                        addToCartButton.text = "Add to Cart"
                        addToCartButton.isEnabled = true
                    }
                }
            }
        }
    }
}
