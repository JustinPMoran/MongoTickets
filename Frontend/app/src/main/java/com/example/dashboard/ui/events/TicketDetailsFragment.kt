package com.example.dashboard.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import api.RetrofitClient
import api.EventApiService
import api.EventDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.dashboard.R

class TicketDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_ticket_details, container, false)

        val eventNameText = root.findViewById<TextView>(R.id.ticketEventName)
        val ticketCostText = root.findViewById<TextView>(R.id.ticketCost)
        val ticketDateText = root.findViewById<TextView>(R.id.ticketDate)
        val ticketQuantityText = root.findViewById<TextView>(R.id.ticketQuantityValue)
        val increaseQuantityButton = root.findViewById<Button>(R.id.increaseQuantityButton)
        val decreaseQuantityButton = root.findViewById<Button>(R.id.decreaseQuantityButton)

        val eventId = 1

        // Set initial ticket quantity
        var ticketQuantity = 1
        ticketQuantityText.text = ticketQuantity.toString()

        // Make the API call to get event details
        val retrofit = RetrofitClient.retrofitInstance
        val apiService = retrofit.create(EventApiService::class.java)

        val call = apiService.getEventDetails(eventId)
        call.enqueue(object : Callback<EventDetails> {
            override fun onResponse(call: Call<EventDetails>, response: Response<EventDetails>) {
                if (response.isSuccessful) {
                    val eventDetails = response.body()
                    eventDetails?.let {
                        eventNameText.text = "Event Name: ${it.name}"
                        ticketDateText.text = "Date: ${it.date}"
                        ticketCostText.text = "Cost: 50 USD" // Set a fixed or dynamic cost based on the event details if applicable
                    }
                } else {
                    eventNameText.text = "Failed to load event details. Error code: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<EventDetails>, t: Throwable) {
                eventNameText.text = "Failed to load event details. Error: ${t.message}"
            }
        })

        // Set button click listeners for increasing/decreasing quantity
        increaseQuantityButton.setOnClickListener {
            ticketQuantity++
            ticketQuantityText.text = ticketQuantity.toString()
        }

        decreaseQuantityButton.setOnClickListener {
            if (ticketQuantity > 1) {
                ticketQuantity--
                ticketQuantityText.text = ticketQuantity.toString()
            }
        }

        return root
    }
}
