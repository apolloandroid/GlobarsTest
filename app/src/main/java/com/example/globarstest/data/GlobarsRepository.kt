package com.example.globarstest.data

import android.util.Log
import com.example.globarstest.data.models.Authorization
import com.example.globarstest.data.models.Vehicle
import com.example.globarstest.data.network.NetworkService
import javax.inject.Inject

class GlobarsRepository @Inject constructor(private val networkService: NetworkService) :
    Repository {
    override suspend fun login(authorization: Authorization) =
        networkService.login(authorization)

    override suspend fun getSessionId(token: String) = networkService.getSessionId(token)

    override suspend fun getVehiclesList(token: String, sessionId: String): List<Vehicle> {
        return networkService.getVehiclesList(token, sessionId)
    }
}