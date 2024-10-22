package com.example.dashboard.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import api.EventApiService
import api.RetrofitClient
import api.EventDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.dashboard.R

class EventDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_event_details, container, false)

        val eventId = arguments?.getInt("eventId") ?: 0
        val eventDetailsText = root.findViewById<TextView>(R.id.eventDetailsText)
        val bookTicketsButton = root.findViewById<Button>(R.id.bookTicketsButton)

        val retrofit = RetrofitClient.retrofitInstance
        val apiService = retrofit.create(EventApiService::class.java)

        val call = apiService.getEventDetails(eventId)
        call.enqueue(object : Callback<EventDetails> {
            override fun onResponse(call: Call<EventDetails>, response: Response<EventDetails>) {
                if (response.isSuccessful) {
                    val eventDetails = response.body()
                    eventDetails?.let {
                        eventDetailsText.text = "Event Name: ${it.name}\nDate: ${it.date}\nLocation: ${it.location}\nDescription: ${it.description}"

                        bookTicketsButton.setOnClickListener {
                            val bundle = Bundle().apply {
                                putString("ticketQuantity", "1") // Example quantity
                            }
                            findNavController().navigate(R.id.action_eventDetailsFragment_to_ticketDetailsFragment, bundle)
                        }
                    }
                } else {
                    eventDetailsText.text = "Failed to load event details. Error code: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<EventDetails>, t: Throwable) {
                eventDetailsText.text = "Failed to load event details. Error: ${t.message}"
            }
        })

        return root
    }
}
