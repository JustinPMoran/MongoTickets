package com.example.dashboard.ui.events

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class EventsLiveTicketPrice : ViewModel() {

    private val _ticketPrice = MutableLiveData<String>()
    val ticketPrice: LiveData<String> = _ticketPrice

    private lateinit var websocket: WebSocket

    fun connectToWebSocket(username: String) {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("ws://coms-3090-074.class.las.iastate.edu:8080/ticket/price/socket/1")  // Use 10.0.2.2 for localhost when testing on Android emulator
            .build()
        //ws://10.0.2.2:8080/chat/$username

        websocket = client.newWebSocket(request, createWebSocketListener())
    }

    private fun createWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Connection opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Received message: $text")
                processMessage(text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closing: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error: ${t.message}", t)
                _ticketPrice.postValue("Unable to load price: ${t.message}")
            }
        }
    }

    private fun processMessage(message: String) {
        // Assuming the message contains the ticket price
        // You may need to adjust this based on the actual message format
        _ticketPrice.postValue(message)
    }

    fun sendMessage(content: String) {
        if (::websocket.isInitialized) {
            websocket.send(content)
        } else {
            Log.e("WebSocket", "WebSocket not initialized")
        }
    }

    fun disconnectWebSocket() {
        if (::websocket.isInitialized) {
            websocket.close(1000, "ViewModel Cleared")
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWebSocket()
    }
}