package com.example.dashboard.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import api.ApiResponse
import com.example.dashboard.R
import api.RetrofitClient
import api.SignupRequest
import api.UserApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        val usernameInput: EditText = view.findViewById(R.id.editTextTextEmailAddress)
        val emailInput: EditText = view.findViewById(R.id.editTextTextEmailAddress2)
        val passwordInput: EditText = view.findViewById(R.id.editTextTextEmailAddress3)
        val signUpButton: Button = view.findViewById(R.id.button)

        signUpButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val signupRequest = SignupRequest(username, email, password)

                // Make the signup request
                val userApiService = RetrofitClient.getUserApiService()
                val call = userApiService.signupUser(signupRequest)
                call.enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show()
                            // You can navigate to the login fragment or home here
                        } else {
                            Toast.makeText(context, "Signup failed: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(context, "Signup request failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
