package com.example.kotlin.model

data class UserModel(
    val userId: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dob: String = "",
    val gender: String = "",
) {
    // Helper property to combine names
    val fullName: String get() = "$firstName $lastName".trim()

    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "email" to email,
            "firstName" to firstName,
            "lastName" to lastName,
            "dob" to dob,
            "gender" to gender
        )
    }
}