package com.example.data.model

data class UserProfile(
    val name: String = "Shubh Anand",
    val email: String = "nobitanobi7209@gmail.com",
    val avatarUrl: String? = null,
    val gradeOrClass: String = "Class 12",
    val isLoggedIn: Boolean = false
)
