package com.example.globarstest.data.models

import java.util.*


data class Session(
    var id: String? = null,
    var userId: String? = null,
    var units: List<Unit>? = null,
    var groups: List<Any>? = null,
    var geoZones: List<Any>? = null,
    var drivers: List<Any>? = null,
    var trailers: List<Any>? = null,
    var trackingSettings: Any? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null
)