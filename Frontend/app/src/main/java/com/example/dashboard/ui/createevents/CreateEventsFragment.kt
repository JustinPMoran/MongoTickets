package com.example.dashboard.ui.createevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import api.EventApiService
import com.example.dashboard.databinding.FragmentCreateEventsBinding
import dataClasses.EventDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateEventsFragment : Fragment() {

    private var _binding: FragmentCreateEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventApiService: EventApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://coms-3090-074.class.las.iastate.edu:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        eventApiService = retrofit.create(EventApiService::class.java)

        // Set up submit button click listener
        binding.submitButton.setOnClickListener {
            submitEvent()
        }
    }

    private fun submitEvent() {
        val name = binding.nameTextView.text.toString()
        val date = binding.dateTextView.text.toString()
        val location = binding.locationTextView.text.toString()
        val description = binding.descriptionTextView.text.toString()
        val maxCapacity = binding.maxCapacityTextView.text.toString()

        if (name.isBlank() || date.isBlank() || location.isBlank() || description.isBlank() || maxCapacity.isBlank()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val createEventDetails = CreateEventDetails(name, date, location, description, maxCapacity)

        eventApiService.createEvent(createEventDetails).enqueue(object : Callback<EventDetails> {
            override fun onResponse(call: Call<EventDetails>, response: Response<EventDetails>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Event created successfully", Toast.LENGTH_SHORT).show()
                    clearFields()
                } else {
                    Toast.makeText(context, "Failed to create event", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventDetails>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearFields() {
        binding.nameTextView.setText("")
        binding.dateTextView.setText("")
        binding.locationTextView.setText("")
        binding.descriptionTextView.setText("")
        binding.maxCapacityTextView.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


