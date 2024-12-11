package com.example.dashboard.ui.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboard.databinding.ItemEventCardBinding
import dataClasses.EventDetails

class EventAdapter(private val onEventClick: (EventDetails) -> Unit) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private var events: List<EventDetails> = emptyList()

    fun updateEvents(newEvents: List<EventDetails>) {
        events = newEvents
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class EventViewHolder(private val binding: ItemEventCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventDetails) {
            binding.apply {
                eventNameText.text = event.name
                eventDetailsText.text = """
                    Date: ${event.date}
                    Location: ${event.location}
                    Description: ${event.description}
                """.trimIndent()

                bookTicketsButton.setOnClickListener {
                    onEventClick(event)
                }

                // Make the entire card clickable
                root.setOnClickListener {
                    onEventClick(event)
                }
            }
        }
    }
}
