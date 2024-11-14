package dataClasses

import com.google.gson.annotations.SerializedName

data class Account(
    val id: Int,
    val username: String?,
    val password: String?,
    val email: String?,
    @SerializedName("join_date") val joinDate: String?,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("store_rating") val storeRating: Int,
    @SerializedName("store_review_count") val storeReviewCount: Int,
    @SerializedName("events_visited_count") val eventsVisitedCount: Int,
    val tickets: List<Ticket>?
)