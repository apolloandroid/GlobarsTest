package com.example.globarstest.data.network.responsemodels

import com.example.globarstest.data.models.Vehicle

class GetVehicleResponse(
    val success: Boolean,
    val data: List<Vehicle>
)