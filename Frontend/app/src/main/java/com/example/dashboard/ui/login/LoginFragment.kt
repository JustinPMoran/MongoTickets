package com.example.dashboard.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dashboard.R
import com.example.dashbaord.ui.SignupFragment

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Find the sign-up button
        val signUpButton: Button = view.findViewById(R.id.button2)

        // Set click listener for the sign-up button
//        signUpButton.setOnClickListener {
//            // Create an instance of SignupFragment
//            val signUpFragment = SignupFragment()
//
//            // Replace the current fragment with SignupFragment
//            parentFragmentManager.beginTransaction().apply {
//                replace(R.id.fragment_container, signUpFragment)
//                addToBackStack(null)
//                commit()
//            }
//        }

        return view
    }
}