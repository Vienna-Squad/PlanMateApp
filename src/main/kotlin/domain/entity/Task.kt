package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val state: String,
    val assignedTo: List<String>,
    val createdBy: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val projectId: String,
)