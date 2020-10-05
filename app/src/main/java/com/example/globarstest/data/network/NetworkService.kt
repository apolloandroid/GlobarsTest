package com.example.globarstest.data.network

import com.example.globarstest.data.models.Authorization
import com.example.globarstest.data.models.Vehicle
import com.example.globarstest.data.network.responsemodels.AuthorizationResponse
import com.example.globarstest.data.network.responsemodels.GetVehicleResponse

interface NetworkService {
    fun login(authorization: Authorization): AuthorizationResponse
    fun getSessionId(token: String): String
    fun getVehiclesList(token: String, sessionId: String): List<Vehicle>
}