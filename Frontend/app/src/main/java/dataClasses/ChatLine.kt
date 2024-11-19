package dataClasses


import com.google.gson.annotations.SerializedName

data class ChatLine(
    val id: Int,
    @SerializedName("sender_account") val senderAccount: Account?,
    @SerializedName("line_text") val lineText: String?,
    @SerializedName("created_timestamp") val createdTimestamp: String?
)