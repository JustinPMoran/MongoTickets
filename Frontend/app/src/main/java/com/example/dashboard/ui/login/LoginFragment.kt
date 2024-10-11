package com.example.dashboard.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dashboard.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Find the signup button
        val signupButton: Button = view.findViewById(R.id.button2)

        // Set click listener for the signup button
        signupButton.setOnClickListener {
            // Navigate to SignupFragment
            navigateToSignup()
        }

        return view
    }

    private fun navigateToSignup() {
        // Create an instance of SignupFragment
        val signupFragment = SignupFragment()

        // Replace the current fragment with SignupFragment
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.nav_home, signupFragment) // Use the correct container ID
            addToBackStack(null) // Add to back stack if you want to allow back navigation
            commit()
        }
    }
}