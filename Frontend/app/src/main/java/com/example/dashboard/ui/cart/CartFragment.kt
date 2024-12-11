package com.example.dashboard.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboard.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter // Assume you have a CartAdapter class
    private val cartItems = mutableListOf<CartItem>() // Replace with your cart item model

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        cartAdapter = CartAdapter(cartItems)
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        // Load Cart Items (Dummy data here, replace with your data source)
        loadCartItems()

        // Checkout button click listener
        binding.checkoutButton.setOnClickListener {
            // Add your checkout logic here
        }
    }

    private fun loadCartItems() {
        cartItems.add(CartItem("Item 1", 10.99, 1))
        cartItems.add(CartItem("Item 2", 15.49, 2))
        cartItems.add(CartItem("Item 3", 5.99, 1))
        cartAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
