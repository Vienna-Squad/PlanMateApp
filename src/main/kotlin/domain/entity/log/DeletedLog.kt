package org.example.domain.entity.log

import java.time.LocalDateTime
import java.util.UUID

class DeletedLog(
    username: String,
    affectedId: UUID,
    affectedName: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    val deletedFrom: String? = null,
) : Log(username, affectedId, affectedName, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.DELETED.name.lowercase()} ${affectedType.name.lowercase()} $affectedName [$affectedId] ${if (!deletedFrom.isNullOrBlank()) "from $deletedFrom" else ""} at $dateTime"
}