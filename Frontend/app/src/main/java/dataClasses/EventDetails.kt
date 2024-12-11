package dataClasses

import com.google.gson.annotations.SerializedName

data class EventDetails(
    val id: Int,
    val name: String,
    val date: String,
    val location: String,
    val description: String,
    @SerializedName("max_capacity") val maxCapacity: Int,
    // Other properties as needed
)
