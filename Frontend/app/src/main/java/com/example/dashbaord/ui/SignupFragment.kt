package com.example.dashbaord.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dashbaord.R
import com.example.dashbaord.R.*

class SignupFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(layout.fragment_signup, container, false)

        // Find the login button (assuming you have a button to go back to login)
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
            replace(R.id.fragment_container, loginFragment)
            // No need to add to back stack if you want to remove SignupFragment from the stack
            commit()
        }
    }
}