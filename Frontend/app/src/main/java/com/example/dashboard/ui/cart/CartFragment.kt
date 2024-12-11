package com.example.dashboard.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dashboard.R
import com.example.dashboard.databinding.FragmentCartBinding
import dataClasses.CartItem

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter
    private val checkoutButton by lazy { binding.checkoutButton }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartAdapter = CartAdapter(CartManager.getCartItems().toMutableList()) { cartItem ->
            removeFromCart(cartItem)
        }

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        // Load cart items
        loadCartItems()
        binding.checkoutButton.setOnClickListener {
            if (CartManager.getCartItems().isNotEmpty()) {
                findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
            } else {
                Toast.makeText(requireContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeFromCart(cartItem: CartItem) {
        val accountId = 5 // Replace with the actual user ID
        CartManager.removeFromCart(cartItem.ticketId, accountId) { success ->
            if (success) {
                // Update the adapter and notify changes
                val updatedItems = CartManager.getCartItems().toMutableList()
                cartAdapter.items.clear()
                cartAdapter.items.addAll(updatedItems)
                cartAdapter.notifyDataSetChanged()
                Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCartItems() {
        val accountId = 5 // Replace with the actual account ID
        CartManager.fetchCart(accountId) { success ->
            if (success) {
                val updatedItems = CartManager.getCartItems()
                cartAdapter.items.clear()
                cartAdapter.items.addAll(updatedItems)
                cartAdapter.notifyDataSetChanged() // Notify the adapter to refresh
            } else {
                Toast.makeText(context, "Failed to load cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
