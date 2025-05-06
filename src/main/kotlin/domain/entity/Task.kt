package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val state: String,
    val assignedTo: List<UUID> = emptyList(),
    val createdBy: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val projectId: UUID,
){
    override fun toString(): String {
        return """
            Task ID: $id
            Title: $title
            State: $state
            Assigned To: ${assignedTo.joinToString(", ")}
            Created By: $createdBy
            Created At: $createdAt
            Project ID: $projectId
        """.trimIndent()
    }
}