package org.example.domain.entity

import java.util.UUID

data class State(
    val id: UUID = UUID.randomUUID(),
    val name: String
) {
    override fun toString() = "$id:$name"
}