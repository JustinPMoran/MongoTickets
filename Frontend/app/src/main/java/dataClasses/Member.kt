package dataClasses

data class Member(
    val id: Int,
    val username: String,
    val email: String,
    val tickets: List<Ticket>
)
