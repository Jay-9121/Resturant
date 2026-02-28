// File: com.example.kotlin.model.BookingModel.kt
package com.example.kotlin.model

data class BookingModel(
    var bookingId: String = "",
    var restaurantId: String = "",
    var restaurantName: String = "",
    var userEmail: String = "",
    var date: String = "",
    var time: String = "",
    var status: String = "Pending", // Pending, Approved, Declined
    var tableNo: String = ""
)