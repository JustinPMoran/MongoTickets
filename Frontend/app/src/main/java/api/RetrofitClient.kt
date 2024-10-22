package api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://954d61fd-9433-4b8c-9551-ff08de7e6253.mock.pstmn.io"

    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getUserApiService(): UserApiService {
        return retrofitInstance.create(UserApiService::class.java)
    }
}
