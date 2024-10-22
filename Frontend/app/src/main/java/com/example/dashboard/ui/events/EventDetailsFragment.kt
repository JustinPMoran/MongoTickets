package com.example.dashboard.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
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

        val eventImage = root.findViewById<ImageView>(R.id.eventImage)
        val eventNameText = root.findViewById<TextView>(R.id.eventNameText)
        val eventDetailsText = root.findViewById<TextView>(R.id.eventDetailsText)
        val bookTicketsButton = root.findViewById<Button>(R.id.bookTicketsButton)

        // Assuming we already have a Retrofit instance
        val retrofit = RetrofitClient.retrofitInstance
        val apiService = retrofit.create(EventApiService::class.java)

        val call = apiService.getEventDetails(eventId)
        call.enqueue(object : Callback<EventDetails> {
            override fun onResponse(call: Call<EventDetails>, response: Response<EventDetails>) {
                if (response.isSuccessful) {
                    val eventDetails = response.body()
                    eventDetails?.let {
                        // Setting event details in UI
                        eventNameText.text = it.name
                        eventDetailsText.text = "Date: ${it.date}\nLocation: ${it.location}\nDescription: ${it.description}"
                        // Load the image with a library like Glide or Picasso if needed.
                        // e.g., Glide.with(this).load(it.imageUrl).into(eventImage)
                    }
                } else {
                    eventDetailsText.text = "Failed to load event details. Error code: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<EventDetails>, t: Throwable) {
                eventDetailsText.text = "Failed to load event details. Error: ${t.message}"
            }
        })

        bookTicketsButton.setOnClickListener {
            // Handle the ticket booking action here
            // For example, navigate to another fragment or show a dialog
        }

        return root
    }
}
