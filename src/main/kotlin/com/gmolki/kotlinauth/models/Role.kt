package com.gmolki.kotlinauth.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "roles")
data class Role(
    @MongoId
    val id: ObjectId,

    var name: ERole,
)