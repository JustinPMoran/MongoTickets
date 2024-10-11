package api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EventApiService {
    @GET("events/{eventId}")
    fun getEventDetails(@Path("eventId") eventId: Int): Call<EventDetails>
}
