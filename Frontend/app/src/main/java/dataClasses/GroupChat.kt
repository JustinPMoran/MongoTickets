package dataClasses

data class GroupChat(
    val id: Int,
    val lines: List<ChatLine>?,
    val members: List<Account>?
)
