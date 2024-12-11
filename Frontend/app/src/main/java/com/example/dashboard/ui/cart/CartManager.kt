package com.example.dashboard.ui.cart

import api.ApiResponse
import api.RetrofitClient
import api.UserApiService
import dataClasses.CartItem
import dataClasses.Ticket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CartManager {
    private val internalCartItems = mutableListOf<CartItem>()

    fun addToCart(cartItem: CartItem, accountId: Int, callback: (Boolean) -> Unit) {
        val retrofit = RetrofitClient.retrofitInstance
        val apiService = retrofit.create(UserApiService::class.java)

        apiService.addToCart(cartItem.ticketId, accountId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.message == "success") {
                        internalCartItems.add(cartItem)
                        callback(true)
                    } else {
                        callback(false)
                    }
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                callback(false)
            }
        })
    }


    fun removeFromCart(ticketId: Int, accountId: Int, callback: (Boolean) -> Unit) {
        val retrofit = RetrofitClient.retrofitInstance
        val apiService = retrofit.create(UserApiService::class.java)

        apiService.removeFromCart(ticketId, accountId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.message == "success") {
                    // Remove from the internal cart items
                    internalCartItems.removeAll { it.ticketId == ticketId }
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                callback(false)
            }
        })
    }


    fun fetchCart(accountId: Int, callback: (Boolean) -> Unit) {
        val retrofit = RetrofitClient.retrofitInstance
        val apiService = retrofit.create(UserApiService::class.java)

        apiService.getCart(accountId).enqueue(object : Callback<List<Ticket>> {
            override fun onResponse(call: Call<List<Ticket>>, response: Response<List<Ticket>>) {
                if (response.isSuccessful) {
                    response.body()?.let { tickets ->
                        internalCartItems.clear()
                        internalCartItems.addAll(tickets.map { ticket ->
                            CartItem(
                                ticketId = ticket.id ?: 0, // Default to 0 if null
                                section = ticket.section ?: "Unknown", // Default to "Unknown" if null
                                row = ticket.row ?: "Unknown", // Default to "Unknown" if null
                                ticketCost = ticket.price ?: 0.0, // Default to 0.0 if null
                                ticketQuantity = 1 // Default quantity
                            )
                        })
                        // Debugging log
                        println("Fetched Cart Items: $internalCartItems")
                        callback(true)
                    } ?: callback(false)
                } else {
                    println("API Error: ${response.code()} - ${response.message()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                println("API Failure: ${t.message}")
                callback(false)
            }
        })
    }


    fun getCartItems(): List<CartItem> = internalCartItems
}
