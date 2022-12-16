package com.gmolki.kotlinauth.dtos.responses

data class JwtResponse(
    var accessToken: String,
    var id: String,
    var username: String,
    var email: String,
    val roles: List<String>,
    var type: String = "Bearer",
)