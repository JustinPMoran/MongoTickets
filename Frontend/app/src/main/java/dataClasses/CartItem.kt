package dataClasses

data class CartItem(
    val ticketId: Int,
    val section: String,
    val row: String,
    val ticketCost: Double,
    var ticketQuantity: Int
)
