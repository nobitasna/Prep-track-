package com.example.data.model

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val avatarUrl: String? = null,
    val uid: String? = null,
    val gradeOrClass: String = "Class 12",
    val isLoggedIn: Boolean = false
)
