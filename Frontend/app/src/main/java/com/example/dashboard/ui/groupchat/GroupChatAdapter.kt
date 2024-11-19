package com.example.dashboard.ui.groupchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dataClasses.GroupChat
import com.example.dashboard.R

class GroupChatAdapter(
    private val groupChats: List<GroupChat>,
    private val onChatClick: (GroupChat) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<GroupChatAdapter.GroupChatViewHolder>() {

    inner class GroupChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatName: TextView = itemView.findViewById(R.id.chatName)
        val openChatButton: Button = itemView.findViewById(R.id.chatButton)

        fun bind(groupChat: GroupChat) {
            val memberNames = groupChat.members?.joinToString { it.username ?: "Unknown" } ?: "No members"
            chatName.text = "Chat ID: ${groupChat.id} - Members: $memberNames"
            openChatButton.setOnClickListener {
                onChatClick(groupChat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_chat, parent, false)
        return GroupChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupChatViewHolder, position: Int) {
        holder.bind(groupChats[position])
    }

    override fun getItemCount(): Int = groupChats.size
}
