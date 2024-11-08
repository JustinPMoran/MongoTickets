package com.example.dashboard.ui.groupchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboard.R
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GroupChatFragment : Fragment() {

    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button  // Changed from ImageButton to Button
    private lateinit var createGroupButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var websocket: WebSocket
    private val messageList = mutableListOf<Message>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_groupchat, container, false)

        messageInput = root.findViewById(R.id.messageInput)
        sendButton = root.findViewById(R.id.sendButton)
        createGroupButton = root.findViewById(R.id.createGroupButton)
        recyclerView = root.findViewById(R.id.messageRecyclerView)

        messageAdapter = MessageAdapter(messageList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = messageAdapter

        setupWebSocket()

        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessageToWebSocket(messageText)
                messageInput.text.clear()
            }
        }

        createGroupButton.setOnClickListener {
            // Logic to create group by adding friends
            openCreateGroupDialog()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        websocket.close(1000, "Fragment Closed")
    }

    private fun setupWebSocket() {
        val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build()
        val request = Request.Builder().url("wss://yourserver.com/groupchat").build()
        websocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val jsonObject = JSONObject(text)
                val message = Message(
                    sender = jsonObject.getString("sender"),
                    content = jsonObject.getString("content")
                )
                activity?.runOnUiThread {
                    messageList.add(message)
                    messageAdapter.notifyItemInserted(messageList.size - 1)
                    recyclerView.scrollToPosition(messageList.size - 1)
                }
            }
        })
    }

    private fun sendMessageToWebSocket(messageText: String) {
        val jsonMessage = JSONObject().apply {
            put("sender", "YourUserName")
            put("content", messageText)
        }
        websocket.send(jsonMessage.toString())
    }

    private fun openCreateGroupDialog() {
        // Logic to open a dialog to create a group by adding friends
        // This could be a dialog where users can select friends and rename the group chat
    }
}
