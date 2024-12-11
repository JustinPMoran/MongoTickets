package com.example.dashboard.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import api.ApiResponse
import api.RetrofitClient
import api.UserApiService
import com.example.dashboard.R
import com.example.dashboard.ui.cart.CartManager
import dataClasses.CartItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutFragment : Fragment() {

    private lateinit var cartItems: List<CartItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_checkout, container, false)

        val itemsSummaryTextView = root.findViewById<TextView>(R.id.itemsSummaryTextView)
        val totalAmountTextView = root.findViewById<TextView>(R.id.totalAmountTextView)
        val fullNameEditText = root.findViewById<EditText>(R.id.fullNameEditText)
        val cardNumberEditText = root.findViewById<EditText>(R.id.cardNumberEditText)
        val cvvEditText = root.findViewById<EditText>(R.id.cvvEditText)
        val addressEditText = root.findViewById<EditText>(R.id.addressEditText)
        val phoneNumberEditText = root.findViewById<EditText>(R.id.phoneNumberEditText)
        val placeOrderButton = root.findViewById<Button>(R.id.placeOrderButton)

        // Fetch cart items and calculate total
        cartItems = CartManager.getCartItems()
        var totalAmount = 0.0
        val summary = StringBuilder()
        cartItems.forEach { cartItem ->
            summary.append(
                "${cartItem.section}, Row ${cartItem.row} (x${cartItem.ticketQuantity}): $${
                    String.format(
                        "%.2f",
                        cartItem.ticketCost * cartItem.ticketQuantity
                    )
                }\n"
            )
            totalAmount += cartItem.ticketCost * cartItem.ticketQuantity
        }

        // Display summary and total
        itemsSummaryTextView.text = summary.toString()
        totalAmountTextView.text = "Total: $${String.format("%.2f", totalAmount)}"

        // Place Order button functionality
        placeOrderButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val cardNumber = cardNumberEditText.text.toString().trim()
            val cvv = cvvEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()

            if (fullName.isBlank() || cardNumber.isBlank() || cvv.isBlank() || address.isBlank() || phoneNumber.isBlank()) {
                Toast.makeText(context, "Please fill in all payment details.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                placeOrder(totalAmount)
            }
        }

        return root
    }

    private fun placeOrder(totalAmount: Double) {
        val apiService = RetrofitClient.retrofitInstance.create(UserApiService::class.java)

        val accountId = 5
        val ticketId = 50
        val eventId = 2

        apiService.createTransaction(
            accountId = accountId,
            ticketId = ticketId,
            eventId = eventId,
            amountPaid = totalAmount
        ).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.message == "success") {
                    Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                    CartManager.clearCart(accountId = 5) { success ->
                        if (success) {
                            Toast.makeText(context, "Cart cleared successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to clear the cart.", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(context, "Failed to place order.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
