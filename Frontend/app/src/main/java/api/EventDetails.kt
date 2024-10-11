package api

data class EventDetails(
    val id: Int,
    val name: String,
    val date: String,
    val location: String,
    val description: String,
    val imageUrl: String,
    // Other properties as needed
)
