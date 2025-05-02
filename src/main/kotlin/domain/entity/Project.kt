package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Project(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val states: List<String>,
    val createdBy: UUID,
    val cratedAt: LocalDateTime = LocalDateTime.now(),
    val matesIds: List<UUID>
)
