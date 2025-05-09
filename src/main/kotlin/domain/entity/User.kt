package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val username: String,
    val hashedPassword: String,//hashed using MD5
    val role: UserRole,
    val cratedAt: LocalDateTime = LocalDateTime.now(),
){
    enum class UserRole { ADMIN, MATE }
}

