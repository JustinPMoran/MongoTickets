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
import com.example.dashboard.R
import api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val emailInput: EditText = view.findViewById(R.id.editTextTextEmailAddress2)
        val passwordInput: EditText = view.findViewById(R.id.editTextTextEmailAddress3)
        val loginButton: Button = view.findViewById(R.id.button)
        val signupButton: Button = view.findViewById(R.id.button2)

        signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_nav_signup)
        }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (validateInput(email, password)) {
                performLogin(email, password)
            }
        }

        return view
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (password.isEmpty() || password.length < 1) {
            Toast.makeText(
                context,
                "Password must be at least 1 character long",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun performLogin(email: String, password: String) {
        val userApiService = RetrofitClient.getUserApiService()
        val call = userApiService.loginUser(email, password)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_nav_login_to_nav_gall_home)
                } else {
                    Toast.makeText(
                        context,
                        "Login failed: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
