package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val states: List<String>,
    val createdBy: String,
    val cratedAt: LocalDateTime = LocalDateTime.now(),
    val matesIds: List<String>
)
