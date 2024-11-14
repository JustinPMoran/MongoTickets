package com.example.dashboard.ui.groupchat
import dataClasses.ChatLine
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import org.json.JSONObject
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.RetrofitClient
import dataClasses.ChatCreationResponse
import dataClasses.Friend
import com.example.dashboard.R
import com.example.dashboard.databinding.FragmentGroupChatBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import dataClasses.GroupChat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class GroupChatFragment : Fragment() {

    private var _binding: FragmentGroupChatBinding? = null
    private val binding get() = _binding!!
    private val groupChatList = mutableListOf<GroupChat>()
    private lateinit var groupChatAdapter: GroupChatAdapter
    private var webSocket: WebSocket? = null
    private val selectedFriends = mutableListOf<Friend>()
    private val friendList = mutableListOf<Friend>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        groupChatAdapter = GroupChatAdapter(
            groupChatList,
            onChatClick = { groupChat -> openChatInterface(groupChat.id) },
            context = requireContext()
        )

        binding.groupChatRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.groupChatRecyclerView.adapter = groupChatAdapter

        fetchGroupChats()

        binding.createGroupButton.setOnClickListener {
            loadFriendsAndOpenCreateGroupDialog()
        }

        return root
    }

    private fun fetchGroupChats() {
        val userId = getUserId()
        val call = RetrofitClient.getUserApiService().getGroupChats(userId)
        call.enqueue(object : Callback<List<GroupChat>> {
            override fun onResponse(call: Call<List<GroupChat>>, response: Response<List<GroupChat>>) {
                if (response.isSuccessful) {
                    groupChatList.clear()
                    response.body()?.let { groupChatList.addAll(it) }
                    groupChatAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Failed to load group chats", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GroupChat>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadFriendsAndOpenCreateGroupDialog() {
        RetrofitClient.getUserApiService().getFriends(getUserId())
            .enqueue(object : Callback<List<Friend>> {
                override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                    if (response.isSuccessful) {
                        friendList.clear()
                        friendList.addAll(response.body() ?: emptyList())
                        openCreateGroupDialog()
                    } else {
                        Toast.makeText(context, "Failed to load friends", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                    Toast.makeText(context, "Error loading friends: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun openCreateGroupDialog() {
        val friendNames = friendList.map { it.username }.toTypedArray()
        val selectedItems = BooleanArray(friendList.size)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select Friends for Group Chat")
            .setMultiChoiceItems(friendNames, selectedItems) { _, which, isChecked ->
                if (isChecked) {
                    selectedFriends.add(friendList[which])
                } else {
                    selectedFriends.remove(friendList[which])
                }
            }
            .setPositiveButton("Confirm") { _, _ ->
                createGroupChat()
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun createGroupChat() {
        RetrofitClient.getUserApiService().createChat().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val chatId = responseBody?.substringAfter("Chat ID: ")?.trim()?.toIntOrNull()
                    if (chatId != null) {
                        addMembersToChat(chatId)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Error creating group chat: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addMembersToChat(chatId: Int) {
        selectedFriends.forEach { friend ->
            RetrofitClient.getUserApiService().addMemberToChat(chatId, friend.id)
                .enqueue(object : Callback<ChatCreationResponse> {
                    override fun onResponse(call: Call<ChatCreationResponse>, response: Response<ChatCreationResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "${friend.username} added to chat", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ChatCreationResponse>, t: Throwable) {
                        Toast.makeText(context, "Failed to add ${friend.username}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun openChatInterface(chatId: Int) {
        val userId = getUserId()
        val chatDialogFragment = ChatDialogFragment.newInstance(chatId, userId)
        chatDialogFragment.show(childFragmentManager, "ChatDialogFragment")
    }


    private fun fetchChatHistory(chatId: Int, chatMessages: TextView, onHistoryLoaded: () -> Unit) {
        RetrofitClient.getUserApiService().getChatLines(chatId).enqueue(object : Callback<List<ChatLine>> {
            override fun onResponse(call: Call<List<ChatLine>>, response: Response<List<ChatLine>>) {
                if (response.isSuccessful) {
                    response.body()?.let { chatLines ->
                        chatLines.forEach { line ->
                            val sender = line.senderAccount?.username ?: "Unknown"
                            val message = line.lineText ?: "No message content"
                            val timestamp = line.createdTimestamp ?: "Unknown time"
                            chatMessages.append("$timestamp\n$sender: $message\n\n")
                        }
                    }
                    onHistoryLoaded()
                }
            }

            override fun onFailure(call: Call<List<ChatLine>>, t: Throwable) {
                chatMessages.append("Error loading chat history: ${t.message}\n")
                onHistoryLoaded()
            }
        })
    }

    private fun initializeWebSocket(chatId: Int, chatMessages: TextView) {
        val userId = getUserId()
        val url = "ws://coms-3090-074.class.las.iastate.edu:8080/chat/$chatId/$userId"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                activity?.runOnUiThread {
                    chatMessages.append("Connected to chat\n")
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                activity?.runOnUiThread {
                    chatMessages.append("Received: $text\n")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                activity?.runOnUiThread {
                    chatMessages.append("Connection error: ${t.message}\n")
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                activity?.runOnUiThread {
                    chatMessages.append("Disconnected\n")
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserId(): Int {
        return 5
    }
}