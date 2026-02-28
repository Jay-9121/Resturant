// File: com.example.kotlin.repository.RestaurantRepo.kt
package com.example.kotlin.repository

import com.example.kotlin.model.RestaurantModel

interface RestaurantRepo {
    fun addRestaurant(restaurantModel: RestaurantModel, callback: (Boolean, String) -> Unit)
    fun fetchAllRestaurants(callback: (List<RestaurantModel>?, Boolean, String) -> Unit)
    fun updateRestaurant(restaurantId: String, data: Map<String, Any?>, callback: (Boolean, String) -> Unit)
    fun deleteRestaurant(restaurantId: String, callback: (Boolean, String) -> Unit)
}