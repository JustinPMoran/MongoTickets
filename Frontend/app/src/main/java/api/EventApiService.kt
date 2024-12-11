package api

import com.example.dashboard.ui.createevents.CreateEventDetails
import dataClasses.EventDetails
import dataClasses.Ticket
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventApiService {
    @GET("events/{eventId}")
    fun getEventDetails(@Path("eventId") eventId: Int): Call<EventDetails>

    @GET("events")
    fun getAllEvents(): Call<List<EventDetails>>

    @GET("tickets")
    fun getTickets(): Call<List<Ticket>>

    @POST("events")
    fun createEvent(@Body eventDetails: CreateEventDetails): Call<EventDetails>
}
