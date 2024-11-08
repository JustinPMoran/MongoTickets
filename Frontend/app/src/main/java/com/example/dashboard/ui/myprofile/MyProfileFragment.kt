package com.example.dashboard.ui.myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dashboard.R
import api.UserSession

class MyProfileFragment : Fragment() {

    private lateinit var emailTextView: TextView
    private lateinit var usernameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myprofile, container, false)
        emailTextView = view.findViewById(R.id.emailTextView)
        usernameTextView = view.findViewById(R.id.usernameTextView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the email from UserSession
        val userEmail = UserSession.getUserEmail() ?: "No email available"

        // Display the email in both TextViews
        displayEmail(userEmail)
    }

    private fun displayEmail(email: String) {
        emailTextView.text = email
        usernameTextView.text = email
    }
}