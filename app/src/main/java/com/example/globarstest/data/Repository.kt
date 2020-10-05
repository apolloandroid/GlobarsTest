package com.example.globarstest.data

import com.example.globarstest.data.models.Authorization
import com.example.globarstest.data.models.Session
import com.example.globarstest.data.models.Vehicle
import com.example.globarstest.data.network.responsemodels.AuthorizationResponse

interface Repository {
    suspend fun login(authorization: Authorization): AuthorizationResponse
    suspend fun getSessionId(token: String): String
    suspend fun getVehiclesList(token: String, sessionId: String): List<Vehicle>
}