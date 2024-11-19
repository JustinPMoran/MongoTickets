package com.example.dashboard.ui.friends

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dataClasses.Friend
import com.example.dashboard.R

class FriendsAdapter(
    private val friendsList: MutableList<Friend>,
    private val onRemoveClick: (Friend) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendId: TextView = itemView.findViewById(R.id.friendId)
        val friendName: TextView = itemView.findViewById(R.id.friendName)
        val friendEmail: TextView = itemView.findViewById(R.id.friendEmail)
        val removeFriendButton: Button = itemView.findViewById(R.id.removeFriendButton)

        init {
            removeFriendButton.setOnClickListener {
                val friend = friendsList[adapterPosition]
                // Show confirmation dialog before removing friend
                showRemoveFriendConfirmationDialog(friend)
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
        // Check if friend already exists in the list
        if (friendsList.none { it.id == friend.id }) {
            friendsList.add(friend)
            notifyItemInserted(friendsList.size - 1)
        } else {
            Toast.makeText(context, "Friend with ID ${friend.id} is already in the list.", Toast.LENGTH_SHORT).show()
        }
    }

    fun removeFriend(friend: Friend) {
        val position = friendsList.indexOf(friend)
        if (position != -1) {
            friendsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun showRemoveFriendConfirmationDialog(friend: Friend) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Remove Friend")
            .setMessage("Are you sure you want to remove ${friend.username}?")
            .setPositiveButton("Yes") { _, _ ->
                removeFriend(friend)
                onRemoveClick(friend)
                Toast.makeText(context, "${friend.username} has been removed.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .create()

        // Customize button colors
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.black))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        dialog.show()
    }
}
