package com.example.kotlin.model

data class RestaurantModel(
    var id: String = "",
    var name: String = "",
    var cuisine: String = "",
    var location: String = "",
    var priceRange: String = "",
    var rating: Double = 0.0,
    var imageUrl: String = "",
    var description: String = "",
    var deal: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    // CHANGED: List of User UIDs who saved this restaurant
    var savedBy: List<String> = emptyList()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "cuisine" to cuisine,
            "location" to location,
            "priceRange" to priceRange,
            "rating" to rating,
            "imageUrl" to imageUrl,
            "description" to description,
            "deal" to deal,
            "latitude" to latitude,
            "longitude" to longitude,
            "savedBy" to savedBy
        )
    }
}