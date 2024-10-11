package com.example.dashboard.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dashboard.R

class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        // Find the login button
        val loginButton: Button = view.findViewById(R.id.button20)

        // Set click listener for the login button
        loginButton.setOnClickListener {
            // Navigate back to LoginFragment
            navigateToLogin()
        }

        return view
    }

    private fun navigateToLogin() {
        // Create an instance of LoginFragment
        val loginFragment = LoginFragment()

        // Replace the current fragment with LoginFragment
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.nav_home, loginFragment) // Use the correct container ID
            addToBackStack(null) // Add to back stack if you want to allow back navigation
            commit()
        }
    }
}