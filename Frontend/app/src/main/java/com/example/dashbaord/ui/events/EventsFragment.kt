package com.example.dashbaord.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.dashbaord.databinding.FragmentEventsBinding
import org.json.JSONObject

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val eventsViewModel =
            ViewModelProvider(this).get(EventsViewModel::class.java)

        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up button click listener
        binding.buttonTESTING.setOnClickListener {
            updateIsActive()
        }

        return root
    }

    private fun updateIsActive() {
        val url = "http://coms-3090-074.class.las.iastate.edu:8080/users"

        val jsonObject = JSONObject().apply {
            put("id", 1)
            put("name", "Anoop")
            put("emailId", "anoop@iastate.edu")
            put("ticket", JSONObject().apply {
                put("id", 1)
                put("event_name", "Football")
                put("event_date", "Sep 23")
                put("price", 24.99)
                put("section", "G7")
                put("row", 8)
            })
            put("isActive", false)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url, jsonObject,
            { response ->
                Toast.makeText(requireContext(), "IsActive updated successfully", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(requireContext(), "Error updating isActive: ${error.message}", Toast.LENGTH_LONG).show()
            })

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}