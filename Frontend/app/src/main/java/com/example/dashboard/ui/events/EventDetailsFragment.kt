package com.example.dashboard.ui.events

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import api.EventApiService
import api.RetrofitClient
import api.EventDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.dashboard.R

class EventDetailsFragment : Fragment() {

    private lateinit var viewModel: EventsLiveTicketPrice
    private lateinit var averageTicketPriceButton: Button
    private lateinit var eventDetailsText: TextView
    private lateinit var bookTicketsButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_event_details, container, false)

        eventDetailsText = root.findViewById(R.id.eventDetailsText)
        bookTicketsButton = root.findViewById(R.id.bookTicketsButton)
        averageTicketPriceButton = root.findViewById(R.id.average_ticket_price_button)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(EventsLiveTicketPrice::class.java)

        // Observe live ticket price
        viewModel.ticketPrice.observe(viewLifecycleOwner) { price ->
            averageTicketPriceButton.text = "Average Ticket Price: $price"
        }

        // Connect to WebSocket for live ticket price updates
        viewModel.connectToWebSocket("AndroidUser")  // You can replace "AndroidUser" with an actual username

        // Load event details
        loadEventDetails()

        return root
    }

    private fun loadEventDetails() {
        val eventId = 1 // Replace with actual event ID if needed
        val retrofit = RetrofitClient.retrofitInstance
        val apiService = retrofit.create(EventApiService::class.java)

        val call = apiService.getEventDetails(eventId)
        call.enqueue(object : Callback<EventDetails> {
            override fun onResponse(call: Call<EventDetails>, response: Response<EventDetails>) {
                Log.d("EventDetailsFragment", "Response received in ${response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis} ms")

                if (response.isSuccessful) {
                    val eventDetails = response.body()
                    eventDetails?.let {
                        updateUI(it)
                    }
                } else {
                    val errorMessage = "Failed to load event details. Error code: ${response.code()}"
                    eventDetailsText.text = errorMessage
                    Log.e("EventDetailsFragment", errorMessage)
                }
            }

            override fun onFailure(call: Call<EventDetails>, t: Throwable) {
                val errorMessage = "Failed to load event details. Error: ${t.message}"
                eventDetailsText.text = errorMessage
                Log.e("EventDetailsFragment", errorMessage, t)
            }
        })
    }

    private fun updateUI(eventDetails: EventDetails) {
        eventDetailsText.text = """
            Event Name: ${eventDetails.name}
            Date: ${eventDetails.date}
            Location: ${eventDetails.location}
            Description: ${eventDetails.description}
        """.trimIndent()

        bookTicketsButton.setOnClickListener {
            val bundle = Bundle().apply {
                putString("eventId", eventDetails.id.toString())
                putString("eventName", eventDetails.name)
                putString("ticketPrice", viewModel.ticketPrice.value)
            }
            findNavController().navigate(R.id.action_eventDetailsFragment_to_ticketDetailsFragment, bundle)
        }

        // Request ticket price from WebSocket
        viewModel.sendMessage("get_ticket_price")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.disconnectWebSocket()
    }
}