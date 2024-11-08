package com.example.dashboard.ui.friends.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.Friend
import com.example.dashboard.R

class FriendsAdapter(
    private val friendsList: MutableList<Friend>,
    private val onLongClick: (Friend) -> Unit
) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendId: TextView = itemView.findViewById(R.id.friendId)
        val friendName: TextView = itemView.findViewById(R.id.friendName)
        val friendEmail: TextView = itemView.findViewById(R.id.friendEmail)

        init {
            itemView.setOnLongClickListener {
                onLongClick(friendsList[adapterPosition])
                true
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
}
