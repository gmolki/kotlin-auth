package com.gmolki.kotlinauth.repositories

import com.gmolki.kotlinauth.models.ERole
import com.gmolki.kotlinauth.models.Role
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional


interface RoleRepository : MongoRepository<Role, String> {
    fun findByName(name: ERole): Optional<Role>
}