package com.example.globarstest.data.network.responsemodels

import com.example.globarstest.data.models.Session

data class GetSessionIdResponse(
    val success: Boolean,
    val data: List<Session>
)