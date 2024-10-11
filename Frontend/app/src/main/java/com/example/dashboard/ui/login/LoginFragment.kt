package com.example.dashboard.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import api.ApiResponse
import com.example.dashboard.R
import api.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val emailInput: EditText = view.findViewById(R.id.editTextTextEmailAddress2)
        val passwordInput: EditText = view.findViewById(R.id.editTextTextEmailAddress3)
        val loginButton: Button = view.findViewById(R.id.button)
        val signupButton: Button = view.findViewById(R.id.button2)

        // Set click listener for the signup button
        signupButton.setOnClickListener {
            // Navigate to SignupFragment using Navigation Component
            findNavController().navigate(R.id.action_nav_login_to_nav_signup)
        }

        // Set click listener for the login button
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val userApiService = RetrofitClient.getUserApiService()
                val call = userApiService.loginUser(email, password)

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseString = response.body()?.string()
                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_nav_login_to_nav_gall_home)
                        } else {
                            Toast.makeText(context, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(context, "Login request failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }
}
