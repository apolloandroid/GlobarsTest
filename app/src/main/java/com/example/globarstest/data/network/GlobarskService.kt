package com.example.globarstest.data.network

import android.util.Log
import com.example.globarstest.data.models.Authorization
import com.example.globarstest.data.models.Vehicle
import com.example.globarstest.data.network.responsemodels.AuthorizationResponse
import retrofit2.Retrofit
import javax.inject.Inject

class GlobarsService @Inject constructor(retrofit: Retrofit) : NetworkService {

    private val globarsApi = retrofit.create(GlobarsApi::class.java)

    override fun login(authorization: Authorization): AuthorizationResponse {
        val response = globarsApi.login(authorization).execute()
        return response.body()!!
    }

    override fun getSessionId(token: String): String {
        val response = globarsApi.getSession(token).execute()
        return response.body()?.data!![0].id!!
    }

    override fun getVehiclesList(token: String, sessionId: String): List<Vehicle> {
        val response = globarsApi.getVehiclesList(token, sessionId).execute()
        return response.body()?.data!!
    }
}