package com.example.dashboard.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dashboard.R
import dataClasses.CartItem

class CheckoutFragment : Fragment() {

    private var cartItems: List<CartItem> = listOf() // Pass the cart items dynamically

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_checkout, container, false)

        val itemsSummaryTextView = root.findViewById<TextView>(R.id.itemsSummaryTextView)
        val totalAmountTextView = root.findViewById<TextView>(R.id.totalAmountTextView)
        val placeOrderButton = root.findViewById<Button>(R.id.placeOrderButton)

        // Calculate total
        var totalAmount = 0.0
        val summary = StringBuilder()
        cartItems.forEach {
            summary.append("${it.eventName} (x${it.ticketQuantity}): $${it.ticketCost * it.ticketQuantity}\n")
            totalAmount += it.ticketCost * it.ticketQuantity
        }

        // Display summary and total
        itemsSummaryTextView.text = summary.toString()
        totalAmountTextView.text = "Total: $${String.format("%.2f", totalAmount)}"

        // Place Order button functionality
        placeOrderButton.setOnClickListener {
            // Handle order placement
            // Navigate to confirmation page or show a success message
        }

        return root
    }
}
