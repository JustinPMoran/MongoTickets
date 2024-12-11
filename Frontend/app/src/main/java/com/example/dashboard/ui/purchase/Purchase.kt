package com.example.dashboard.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dashboard.R

class Purchase : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_purchase, container, false)

        val purchaseButton: Button = view.findViewById(R.id.button3)

        purchaseButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_purchase_to_createEventsFragment)
        }

        return view
    }
}
