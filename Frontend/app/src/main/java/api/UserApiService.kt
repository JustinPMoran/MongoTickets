package api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class LoginRequest(val email: String, val password: String)
data class SignupRequest(val username: String, val email: String, val password: String)
data class ApiResponse(val message: String, val success: Boolean)

interface UserApiService {
    @GET("accounts/login")
    fun loginUser(
        @Query("email") email: String,
        @Query("pass") password: String
    ): Call<ResponseBody>

    @POST("accounts/signup")
    fun signupUser(@Body signupRequest: SignupRequest): Call<Unit>
}
