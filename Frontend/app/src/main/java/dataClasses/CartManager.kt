package dataClasses

object CartManager {
    val cartItems = mutableListOf<CartItem>()

    fun addToCart(cartItem: CartItem) {
        cartItems.add(cartItem)
    }
}

data class CartItem(
    val eventId: Int,
    val eventName: String,
    val ticketCost: Double,
    val ticketQuantity: Int
)
