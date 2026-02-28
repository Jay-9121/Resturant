package com.example.kotlin.repository

import com.example.kotlin.model.BookingModel
import com.example.kotlin.model.RestaurantModel
import com.google.firebase.database.*

class RestaurantRepoImpl : RestaurantRepo {
    private val db = FirebaseDatabase.getInstance()
    private val restaurantRef = db.getReference("restaurants")
    private val bookingRef = db.getReference("bookings")

    override fun addRestaurant(restaurantModel: RestaurantModel, callback: (Boolean, String) -> Unit) {
        val id = restaurantRef.push().key ?: ""
        restaurantModel.id = id
        restaurantRef.child(id).setValue(restaurantModel).addOnCompleteListener {
            callback(it.isSuccessful, if(it.isSuccessful) "Added" else "Failed")
        }
    }

    override fun fetchAllRestaurants(callback: (List<RestaurantModel>?, Boolean, String) -> Unit) {
        restaurantRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(RestaurantModel::class.java) }
                callback(list, true, "Success")
            }
            override fun onCancelled(error: DatabaseError) { callback(null, false, error.message) }
        })
    }

    fun toggleSaveRestaurant(restaurantId: String, userId: String, currentList: List<String>, callback: (Boolean, String) -> Unit) {
        val newList = currentList.toMutableList()
        if (newList.contains(userId)) newList.remove(userId) else newList.add(userId)

        restaurantRef.child(restaurantId).child("savedBy").setValue(newList)
            .addOnCompleteListener { callback(it.isSuccessful, if(it.isSuccessful) "Success" else "Failed") }
    }

    fun makeBooking(booking: BookingModel, callback: (Boolean, String) -> Unit) {
        val id = bookingRef.push().key ?: ""
        booking.bookingId = id
        bookingRef.child(id).setValue(booking).addOnCompleteListener { callback(it.isSuccessful, "Booking Sent") }
    }

    fun fetchAllBookings(callback: (List<BookingModel>?, Boolean, String) -> Unit) {
        bookingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(BookingModel::class.java) }
                callback(list, true, "Success")
            }
            override fun onCancelled(error: DatabaseError) { callback(null, false, error.message) }
        })
    }

    fun updateBookingStatus(id: String, status: String, tableNo: String, callback: (Boolean, String) -> Unit) {
        val updates = mapOf("status" to status, "tableNo" to tableNo)
        bookingRef.child(id).updateChildren(updates).addOnCompleteListener { callback(it.isSuccessful, "Updated") }
    }

    override fun updateRestaurant(restaurantId: String, data: Map<String, Any?>, callback: (Boolean, String) -> Unit) {
        restaurantRef.child(restaurantId).updateChildren(data).addOnCompleteListener { callback(it.isSuccessful, "Updated") }
    }

    override fun deleteRestaurant(restaurantId: String, callback: (Boolean, String) -> Unit) {
        restaurantRef.child(restaurantId).removeValue().addOnCompleteListener { callback(it.isSuccessful, "Deleted") }
    }
}