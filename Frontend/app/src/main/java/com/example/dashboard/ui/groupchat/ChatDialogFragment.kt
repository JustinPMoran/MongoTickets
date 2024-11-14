package com.example.dashboard.ui.groupchat

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.dashboard.R
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import dataClasses.ChatLine
import api.RetrofitClient

class ChatDialogFragment(private val chatId: Int, private val userId: Int) : DialogFragment() {

    private var webSocket: WebSocket? = null
    private lateinit var chatMessagesContainer: LinearLayout
    private lateinit var chatInput: EditText
    private lateinit var sendButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_chat, container, false)
        chatMessagesContainer = view.findViewById(R.id.chatMessagesContainer)
        chatInput = view.findViewById(R.id.chatInput)
        sendButton = view.findViewById(R.id.sendButton)

        fetchChatHistory()

        sendButton.setOnClickListener {
            val message = chatInput.text.toString()
            if (message.isNotBlank()) {
                webSocket?.send(message)
                addMessageToContainer("You", message, true)
                chatInput.text.clear()
            }
        }

        initializeWebSocket()

        // Prevent the dialog from closing on outside touch
        dialog?.setCanceledOnTouchOutside(false)

        return view
    }

    private fun fetchChatHistory() {
        RetrofitClient.getUserApiService().getChatLines(chatId).enqueue(object : Callback<List<ChatLine>> {
            override fun onResponse(call: Call<List<ChatLine>>, response: Response<List<ChatLine>>) {
                if (response.isSuccessful) {
                    response.body()?.let { chatLines ->
                        chatLines.forEach { line ->
                            val sender = line.senderAccount?.username ?: "Unknown"
                            val message = line.lineText ?: "No message content"
                            // Check if the sender ID matches the user ID to set sent or received style
                            addMessageToContainer(sender, message, line.senderAccount?.id == userId)
                        }
                    }
                } else {
                    addMessageToContainer("System", "Failed to load chat history.", false)
                }
            }

            override fun onFailure(call: Call<List<ChatLine>>, t: Throwable) {
                addMessageToContainer("System", "Error loading chat history: ${t.message}", false)
            }
        })
    }

    private fun initializeWebSocket() {
        val url = "ws://coms-3090-074.class.las.iastate.edu:8080/chat/$chatId/$userId"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                activity?.runOnUiThread {
                    addMessageToContainer("System", "Connected to chat", false)
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                activity?.runOnUiThread {
                    addMessageToContainer("Friend", text, false)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                activity?.runOnUiThread {
                    addMessageToContainer("System", "Connection error: ${t.message}", false)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                activity?.runOnUiThread {
                    addMessageToContainer("System", "Disconnected", false)
                }
            }
        })
    }

    private fun addMessageToContainer(sender: String, message: String, isSent: Boolean) {
        val messageTextView = TextView(requireContext()).apply {
            text = "$sender: $message"
            setPadding(16, 8, 16, 8)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 8, 8, 8)
                gravity = if (isSent) Gravity.END else Gravity.START
            }

            // Apply font and style
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white)) // Set text color to white
            textSize = 16f // Set font size

            background = if (isSent) {
                ContextCompat.getDrawable(requireContext(), R.drawable.sent_message_background)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.received_message_background)
            }
        }
        chatMessagesContainer.addView(messageTextView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webSocket?.close(1000, null)
    }

    companion object {
        fun newInstance(chatId: Int, userId: Int): ChatDialogFragment {
            return ChatDialogFragment(chatId, userId)
        }
    }
}
