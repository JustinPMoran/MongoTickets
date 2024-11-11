package com.example.dashboard.ui.events_liveticketprice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboard.R
import com.google.gson.Gson
import okhttp3.*
import java.text.SimpleDateFormat
import java.util.*

//data class TicketPrice(
//    val symbol: String,
//    val price: Double,
//    val timestamp: Long
//)

class TicketPriceAdapter(private val ticketPrices: MutableList<TicketPrice> = mutableListOf()) :
    RecyclerView.Adapter<TicketPriceAdapter.TicketPriceViewHolder>() {

    class TicketPriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symbolTextView: TextView = itemView.findViewById(R.id.average_ticket_price_button)
        val priceTextView: TextView = itemView.findViewById(R.id.average_ticket_price_button)
        val timestampTextView: TextView = itemView.findViewById(R.id.average_ticket_price_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketPriceViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_event_details, parent, false)
        return TicketPriceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TicketPriceViewHolder, position: Int) {
        val ticketPrice = ticketPrices[position]
        holder.symbolTextView.text = ticketPrice.symbol
        holder.priceTextView.text = String.format("$%.2f", ticketPrice.price)
        holder.timestampTextView.text = formatTimestamp(ticketPrice.timestamp)
    }

    override fun getItemCount(): Int = ticketPrices.size

    fun addTicketPrice(ticketPrice: TicketPrice) {
        ticketPrices.add(0, ticketPrice)
        notifyItemInserted(0)
    }

    private fun formatTimestamp(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(Date(timestamp))
    }

    fun calculateAveragePrice(): Double {
        if (ticketPrices.isEmpty()) return 0.0
        return ticketPrices.map { it.price }.average()
    }

    fun getAveragePriceDisplay(): String {
        val averagePrice = calculateAveragePrice()
        return String.format("Average Price: $%.2f", averagePrice)
    }
}

class EventsLiveTicketPriceFragment : Fragment() {

    private lateinit var ticketPriceAdapter: TicketPriceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var webSocket: WebSocket
    private lateinit var averageTicketPriceButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_details, container, false)

        recyclerView = view.findViewById(R.id.average_ticket_price_button)
        averageTicketPriceButton = view.findViewById(R.id.average_ticket_price_button)

        ticketPriceAdapter = TicketPriceAdapter()
        recyclerView.adapter = ticketPriceAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        setupWebSocket()

        return view
    }

    private fun setupWebSocket() {
        val client = OkHttpClient()
        val request = Request.Builder().url("wss://your-websocket-server-url").build()
        val listener = TicketPriceWebSocketListener()
        webSocket = client.newWebSocket(request, listener)
    }

    inner class TicketPriceWebSocketListener : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            val ticketPrice = parseTicketPrice(text)
            activity?.runOnUiThread {
                ticketPriceAdapter.addTicketPrice(ticketPrice)
                updateAveragePriceButton()
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            activity?.runOnUiThread {
                averageTicketPriceButton.text = "WebSocket connection failed"
            }
        }

        private fun parseTicketPrice(json: String): TicketPrice {
            return Gson().fromJson(json, TicketPrice::class.java)
        }
    }

    private fun updateAveragePriceButton() {
        val averagePriceDisplay = ticketPriceAdapter.getAveragePriceDisplay()
        averageTicketPriceButton.text = averagePriceDisplay
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webSocket.close(1000, "Fragment destroyed")
    }
}