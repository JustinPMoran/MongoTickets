package com.example.dashboard.ui.groupchat
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import api.RetrofitClient
import dataClasses.ChatCreationResponse
import dataClasses.Friend
import com.example.dashboard.databinding.FragmentGroupChatBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import dataClasses.GroupChat
import okhttp3.WebSocket

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
                    groupChatAdapter.notifyDataSetChanged() // Ensures data consistency
                    Log.d("GroupChatFragment", "Group chats fetched successfully")
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

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.BLACK)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.BLACK)
        }

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
                        fetchGroupChats()

                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Error creating group chat: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun addMembersToChat(chatId: Int) {
        // Add the current user to the list of selected friends
        val currentUser = Friend(id = getUserId(), username = "Current User", email = "user@example.com") // Replace with actual user details if available
        val allMembers = selectedFriends.toMutableList()
        allMembers.add(currentUser)

        allMembers.forEach { friend ->
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserId(): Int {
        return 5
    }
}