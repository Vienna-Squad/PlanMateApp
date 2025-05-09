package org.example.domain.entity.log

import java.time.LocalDateTime
import java.util.UUID

class AddedLog(
    username: String,
    affectedId: UUID,
    affectedName: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    val addedTo: String,
) : Log(username, affectedId, affectedName, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.ADDED.name.lowercase()} ${affectedType.name.lowercase()} $affectedName [$affectedId] to $addedTo at $dateTime"
}