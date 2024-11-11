package api

data class ChatCreationResponse(
    val message: String,
    val chatId: Int? = null // Add this only if chat ID is returned as a separate field in the response
)

