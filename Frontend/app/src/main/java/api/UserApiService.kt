package api

import dataClasses.CartItem
import dataClasses.ChatCreationResponse
import dataClasses.ChatLine
import dataClasses.Friend
import dataClasses.GroupChat
import dataClasses.Ticket
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


data class LoginRequest(val email: String, val password: String)
data class SignupRequest(val username: String, val email: String, val password: String)
data class ApiResponse(val message: String, val success: Boolean)

interface UserApiService {
    @POST("accounts/login")
    fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<ResponseBody>

    @POST("accounts/signup")
    fun signupUser(@Body signupRequest: SignupRequest): Call<Unit>

    @GET("/get_friends/{id}")
    fun getFriends(@Path("id") userId: Int): Call<List<Friend>>

    @PUT("/create_friendship/{id1}/{id2}")
    fun createFriendship(@Path("id1") userId: Int, @Path("id2") friendId: Int): Call<Void>

    @DELETE("/remove_friendship/{id1}/{id2}")
    fun removeFriendship(@Path("id1") userId: Int, @Path("id2") friendId: Int): Call<Void>

    // Corrected definition of getFriendDetails
    @GET("accounts/{friendId}")
    fun getFriendDetails(@Path("friendId") friendId: Int): Call<Friend>

    @POST("chats")
    fun createChat(): Call<ResponseBody>

    @GET("accounts/{userId}/chats")
    fun getGroupChats(@Path("userId") userId: Int): Call<List<GroupChat>>

    @PUT("/chats/add_member/{chat_id}/{account_id}")
    fun addMemberToChat(
        @Path("chat_id") chatId: Int,
        @Path("account_id") accountId: Int
    ): Call<ChatCreationResponse>

    @GET("/chats/{chat_id}/lines")
    fun getChatLines(
        @Path("chat_id") chatId: Int
    ): Call<List<ChatLine>>

    @PUT("/add_to_cart")
    fun addToCart(
        @Query("ticketID") ticketID: Int,
        @Query("accountID") accountID: Int
    ): Call<ApiResponse>

    @PUT("/remove_from_cart")
    fun removeFromCart(
        @Query("ticketID") ticketID: Int,
        @Query("accountID") accountID: Int
    ): Call<ApiResponse>

    @GET("/get_cart")
    fun getCart(
        @Query("accountID") accountID: Int
    ): Call<List<Ticket>>

}
