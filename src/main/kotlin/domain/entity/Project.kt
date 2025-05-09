package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Project(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val states: List<State> = emptyList(),
    val createdBy: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val matesIds: List<UUID> = emptyList()
) {
    override fun toString(): String {
        return """
            Project ID: $id
            Name: $name
            States: ${states.map { it.name }}
            Mates IDs: $matesIds
            Created By: $createdBy
            Created At: $createdAt
        """.trimIndent()
    }
}
