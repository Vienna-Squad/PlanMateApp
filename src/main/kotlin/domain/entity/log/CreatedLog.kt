package org.example.domain.entity.log

import java.time.LocalDateTime
import java.util.UUID

class CreatedLog(
    username: String,
    affectedId: UUID,
    affectedName: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
) : Log(username, affectedId, affectedName, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.CREATED.name.lowercase()} ${affectedType.name.lowercase()} $affectedName [$affectedId] at $dateTime"
}