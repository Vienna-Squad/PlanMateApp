package org.example.domain.entity.log

import java.time.LocalDateTime
import java.util.UUID

class ChangedLog(
    username: String,
    affectedId: UUID,
    affectedName: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    val changedFrom: String,
    val changedTo: String,
) : Log(username, affectedId, affectedName, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.CHANGED.name.lowercase()} ${affectedType.name.lowercase()} $affectedName [$affectedId] from $changedFrom to $changedTo at $dateTime"
}