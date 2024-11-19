package dataClasses

import com.google.gson.annotations.SerializedName

data class Ticket(
    val id: Int,
    val row: String?,
    val section: String?,
    val price: Double,
    @SerializedName("is_active") val isActive: Boolean
)