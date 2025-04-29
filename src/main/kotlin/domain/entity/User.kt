package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class User(
    val username: String,
    val password: String,//hashed using MD5
    val type: UserType,
) {
    val id: String = UUID.randomUUID().toString()

    // todo : rename cratedAt -> createdAt
    val cratedAt: LocalDateTime = LocalDateTime.now()
}

enum class UserType { ADMIN, MATE }
