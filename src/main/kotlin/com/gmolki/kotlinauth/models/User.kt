package com.gmolki.kotlinauth.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Document(collection = "users")
data class User(
    @MongoId
    val id: ObjectId = ObjectId(),

    @NotBlank
    @Size(max = 20)
    var username: String,

    @NotBlank
    @Size(max = 50)
    @Email
    var email: String,

    @NotBlank
    @Size(max = 120)
    var password: String,

    @DBRef
    var roles: Set<Role> = HashSet()
)