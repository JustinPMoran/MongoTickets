package com.example.dashboard.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dashboard.R

class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        // Find the sign-up button
        val signUpButton: Button = view.findViewById(R.id.button20)

        // Set click listener for the sign-up button
        signUpButton.setOnClickListener {
            // Navigate back to LoginFragment
            findNavController().navigate(R.id.action_nav_signup_to_nav_login)
        }

        return view
    }
}
