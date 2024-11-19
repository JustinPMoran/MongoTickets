package api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://coms-3090-074.class.las.iastate.edu:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Log all requests and responses
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mongotickets") // Replace "Your-App-Name" with your app's name
                .build()
            chain.proceed(request)
        }
        .dns(okhttp3.Dns.SYSTEM) // Set DNS to system default
        .connectTimeout(10, TimeUnit.SECONDS) // Adjust the timeout as needed
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }



    fun getUserApiService(): UserApiService {
        return retrofitInstance.create(UserApiService::class.java)
    }
}
