package com.example.globarstest.data.network


import com.example.globarstest.data.models.Authorization
import com.example.globarstest.data.network.responsemodels.AuthorizationResponse
import com.example.globarstest.data.network.responsemodels.GetVehicleResponse
import com.example.globarstest.data.network.responsemodels.GetSessionIdResponse
import retrofit2.Call
import retrofit2.http.*

interface GlobarsApi {
    @POST("auth/login")
    fun login(@Body authorization: Authorization): Call<AuthorizationResponse>

    @GET("tracking/sessions")
    fun getSession(@Header("Authorization") token: String): Call<GetSessionIdResponse>

    @GET("tracking/{sessionId}/units")
    fun getVehiclesList(
        @Header("Authorization") token: String,
        @Path("sessionId") sessionId: String
    ): Call<GetVehicleResponse>
}