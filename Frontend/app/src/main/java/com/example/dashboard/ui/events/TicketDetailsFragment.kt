package com.example.dashboard.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.EventApiService
import api.RetrofitClient
import com.example.dashboard.R
import dataClasses.CartItem
import dataClasses.Ticket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketDetailsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ticketAdapter: TicketAdapter
    private val tickets = mutableListOf<Ticket>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_ticket_details, container, false)
        recyclerView = root.findViewById(R.id.recyclerViewTickets)

        // Set up RecyclerView
        ticketAdapter = TicketAdapter(tickets)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ticketAdapter
        }

        fetchTickets()
        return root
    }

    private fun fetchTickets() {
        val apiService = RetrofitClient.retrofitInstance.create(EventApiService::class.java)
        apiService.getTickets().enqueue(object : Callback<List<Ticket>> {
            override fun onResponse(call: Call<List<Ticket>>, response: Response<List<Ticket>>) {
                if (response.isSuccessful) {
                    tickets.clear()
                    tickets.addAll(response.body() ?: emptyList())
                    ticketAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Failed to load tickets", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
