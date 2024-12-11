package com.example.dashboard.ui.events

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dashboard.R
import com.example.dashboard.databinding.FragmentEventDetailsBinding
import api.EventApiService
import api.RetrofitClient
import dataClasses.EventDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailsFragment : Fragment() {

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadEventDetails()

        binding.sellTicketsButton.setOnClickListener {
            findNavController().navigate(R.id.action_eventDetailsFragment_to_nav_purchase)
        }
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { event ->
            findNavController().navigate(R.id.action_eventDetailsFragment_to_ticketDetailsFragment, Bundle().apply {
                putInt("eventId", event.id)
            })
        }
        binding.EventRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }
    }


    private fun loadEventDetails() {
        val apiService = RetrofitClient.retrofitInstance.create(EventApiService::class.java)
        apiService.getAllEvents().enqueue(object : Callback<List<EventDetails>> {
            override fun onResponse(call: Call<List<EventDetails>>, response: Response<List<EventDetails>>) {
                if (response.isSuccessful) {
                    val events = response.body()?.filter { it.name.length >= 2 } ?: emptyList()
                    eventAdapter.updateEvents(events)
                } else {
                    Log.e("EventDetailsFragment", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<EventDetails>>, t: Throwable) {
                Log.e("EventDetailsFragment", "Error: ${t.message}", t)
            }
        })
    }

    private fun navigateToCreateEventsFragment() {
        findNavController().navigate(R.id.action_nav_purchase_to_createEventsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
