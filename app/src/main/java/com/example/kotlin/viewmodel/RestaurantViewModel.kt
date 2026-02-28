// File: com.example.kotlin.viewmodel.RestaurantViewModel.kt
package com.example.kotlin.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kotlin.model.RestaurantModel
import com.example.kotlin.repository.RestaurantRepo

class RestaurantViewModel(private val repo: RestaurantRepo) : ViewModel() {

    // Main list state for displaying restaurants
    private val _restaurants = mutableStateOf<List<RestaurantModel>>(emptyList())
    val restaurants: State<List<RestaurantModel>> = _restaurants

    // Loading state for UI feedback
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchAllRestaurants()
    }

    fun fetchAllRestaurants() {
        _isLoading.value = true
        repo.fetchAllRestaurants { list, success, message ->
            _isLoading.value = false
            if (success && list != null) {
                _restaurants.value = list
            }
        }
    }

    fun addRestaurant(restaurant: RestaurantModel, onResult: (Boolean, String) -> Unit) {
        repo.addRestaurant(restaurant, onResult)
    }

    fun updateRestaurant(id: String, data: Map<String, Any?>, onResult: (Boolean, String) -> Unit) {
        repo.updateRestaurant(id, data, onResult)
    }

    fun deleteRestaurant(id: String, onResult: (Boolean, String) -> Unit) {
        repo.deleteRestaurant(id, onResult)
    }
}