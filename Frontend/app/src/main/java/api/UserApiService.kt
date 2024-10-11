package api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


data class LoginRequest(val email: String, val password: String)
data class SignupRequest(val username: String, val email: String, val password: String)
data class ApiResponse(val message: String, val success: Boolean)

interface UserApiService {
    @POST("users/login")
    fun loginUser(@Body userDetails: UserDetails): Call<ApiResponse>

    @POST("signup")
    fun signupUser(@Body userDetails: UserDetails): Call<ApiResponse>
}
