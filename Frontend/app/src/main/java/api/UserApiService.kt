package api

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
}
