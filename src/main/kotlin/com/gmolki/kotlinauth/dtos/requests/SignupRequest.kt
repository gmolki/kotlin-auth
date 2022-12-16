package com.gmolki.kotlinauth.dtos.requests

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignupRequest(
    @NotBlank
    @Size(min = 3, max = 20)
    var username: String,

    @NotBlank
    @Size(max = 50)
    @Email
    var email: String,

    var roles: Set<String>?,

    @NotBlank
    @Size(min = 6, max = 40)
    var password: String
)