package com.example.dashboard.ui.friends.adapters

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import api.Friend
import com.example.dashboard.R
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import api.RetrofitClient
import ChatLine

class FriendsAdapter(
    private val friendsList: MutableList<Friend>,
    private val onLongClick: (Friend) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendId: TextView = itemView.findViewById(R.id.friendId)
        val friendName: TextView = itemView.findViewById(R.id.friendName)
        val friendEmail: TextView = itemView.findViewById(R.id.friendEmail)
        private val chatButton: Button = itemView.findViewById(R.id.chatButton)

        init {
            itemView.setOnLongClickListener {
                onLongClick(friendsList[adapterPosition])
                true
            }

            chatButton.setOnClickListener {
                openChatDialog(friendsList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friendsList[position]
        holder.friendId.text = "ID: ${friend.id}"
        holder.friendName.text = "Name: ${friend.username}"
        holder.friendEmail.text = "Email: ${friend.email}"
    }

    override fun getItemCount(): Int = friendsList.size

    fun addFriend(friend: Friend) {
        friendsList.add(friend)
        notifyItemInserted(friendsList.size - 1)
    }

    private fun openChatDialog(friend: Friend) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_chat)

        val messageInput = dialog.findViewById<EditText>(R.id.chatInput)
        val messagesView = dialog.findViewById<TextView>(R.id.chatMessages)
        val sendButton = dialog.findViewById<Button>(R.id.sendButton)

        // Load previous chat history
        loadChatHistory(friend.id, messagesView)

        // Initialize WebSocket for real-time messaging
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://coms-3090-074.class.las.iastate.edu:8080/chat/${getUserId()}/${friend.id}")
            .build()

        val webSocket = client.newWebSocket(request, ChatWebSocketListener(messagesView))

        // Send messages through the WebSocket
        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                webSocket.send(message)
                messagesView.append("You: $message\n")
                Log.d("WebSocket", "Sent message: $message")
                messageInput.text.clear()
            }
        }

        // Close WebSocket connection when dialog is dismissed
        dialog.setOnDismissListener {
            webSocket.close(1000, "Chat closed")
        }

        dialog.show()
    }

    private fun loadChatHistory(friendId: Int, messagesView: TextView) {
        RetrofitClient.getUserApiService().getChatLines(friendId)
            .enqueue(object : Callback<List<ChatLine>> {
                override fun onResponse(call: Call<List<ChatLine>>, response: Response<List<ChatLine>>) {
                    if (response.isSuccessful) {
                        val chatLines = response.body()
                        chatLines?.forEach { line ->
                            messagesView.append("${line.senderAccountId}: ${line.lineText}\n")
                        }
                        Log.d("ChatHistory", "Loaded chat history: $chatLines")
                    } else {
                        Toast.makeText(context, "Failed to load chat history", Toast.LENGTH_SHORT).show()
                        Log.e("ChatHistory", "Failed to load chat history: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<ChatLine>>, t: Throwable) {
                    Toast.makeText(context, "Error loading chat history: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("ChatHistory", "Error loading chat history: ${t.message}")
                }
            })
    }

    private fun getUserId(): Int {
        return 5 // Replace with the actual logged-in user ID
    }

    inner class ChatWebSocketListener(private val messagesView: TextView) : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            Log.d("WebSocket", "Connected to WebSocket")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            // Receiving a message from the server
            messagesView.post {
                messagesView.append("Friend: $text\n")
                Log.d("WebSocket", "Received message: $text")
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            Log.e("WebSocket", "Error: ${t.message}")
            messagesView.post {
                Toast.makeText(context, "WebSocket error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "Closing WebSocket: $reason")
            webSocket.close(1000, null)
        }
    }
}
