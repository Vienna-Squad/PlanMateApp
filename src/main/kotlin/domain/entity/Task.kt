package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Task(
    val title: String,
    val state: String,
    val assignedTo: List<User>,
    val createdBy: User,
    val projectId: String
) {
    val id: String = UUID.randomUUID().toString()
    val cratedAt: LocalDateTime = LocalDateTime.now()
}