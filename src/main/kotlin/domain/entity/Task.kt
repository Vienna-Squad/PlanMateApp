package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val state: String,
    val assignedTo: List<UUID>,
    val createdBy: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val projectId: UUID,
)