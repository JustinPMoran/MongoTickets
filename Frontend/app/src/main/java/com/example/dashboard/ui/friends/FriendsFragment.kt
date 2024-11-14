package com.example.dashboard.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dataClasses.Friend
import com.example.dashboard.databinding.FragmentFriendsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import api.RetrofitClient

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private val friendsList = mutableListOf<Friend>()
    private lateinit var friendsAdapter: FriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        friendsAdapter = FriendsAdapter(
            friendsList,
            onRemoveClick = { friend -> removeFriend(friend) },
            context = requireContext()
        )

        binding.friendsList.layoutManager = LinearLayoutManager(context)
        binding.friendsList.adapter = friendsAdapter

        fetchFriends()

        binding.buttonAddFriend.setOnClickListener {
            binding.usernameInputLayout.visibility = if (binding.usernameInputLayout.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        binding.editTextFriendUsername.setOnEditorActionListener { _, _, _ ->
            val friendId = binding.editTextFriendUsername.text.toString().trim()
            if (friendId.isNotEmpty()) {
                addFriend(friendId)
            } else {
                Toast.makeText(context, "Please enter a friend's ID", Toast.LENGTH_SHORT).show()
            }
            true
        }

        return root
    }

    private fun fetchFriends() {
        val userId = getUserId() // Get current user ID
        val call = RetrofitClient.getUserApiService().getFriends(userId)
        call.enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    friendsList.clear()
                    response.body()?.let { friendsList.addAll(it) }
                    friendsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Failed to load friends", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addFriend(friendId: String) {
        val id = friendId.toIntOrNull() ?: return Toast.makeText(context, "Invalid ID", Toast.LENGTH_SHORT).show()
        val userId = getUserId()

        if (friendsList.any { it.id == id }) {
            Toast.makeText(context, "Friend with ID $id is already in the list.", Toast.LENGTH_SHORT).show()
            return
        }

        val call = RetrofitClient.getUserApiService().createFriendship(userId, id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val friendDetailsCall = RetrofitClient.getUserApiService().getFriendDetails(id)
                    friendDetailsCall.enqueue(object : Callback<Friend> {
                        override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                            if (response.isSuccessful) {
                                response.body()?.let { friendsAdapter.addFriend(it) }
                                Toast.makeText(context, "Friend added successfully!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to load friend details", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Friend>, t: Throwable) {
                            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(context, "Failed to add friend on the server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeFriend(friend: Friend) {
        val userId = getUserId()
        val call = RetrofitClient.getUserApiService().removeFriendship(userId, friend.id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    friendsAdapter.removeFriend(friend)
                    Toast.makeText(context, "Friend removed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to remove friend", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserId(): Int {
        return 5 // Replace with actual user ID
    }
}
