package com.gmolki.kotlinauth.dtos.requests

import javax.validation.constraints.NotBlank

data class LoginRequest(
    @NotBlank
    var username: String,

    @NotBlank
    var password: String,
)