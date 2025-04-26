package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Project(
    val name: String,
    val states: List<String>,
    val createdBy: User,
    val mates: List<User>
) {
    val id: String = UUID.randomUUID().toString()
    val cratedAt: LocalDateTime = LocalDateTime.now()
}
