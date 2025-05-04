package org.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

sealed class Log(
    val username: String,
    val affectedId: UUID,
    val affectedType: AffectedType,
    val dateTime: LocalDateTime = LocalDateTime.now()
) {
    enum class ActionType {
        CHANGED,
        ADDED,
        DELETED,
        CREATED
    }

    enum class AffectedType {
        PROJECT,
        TASK,
        MATE,
        STATE
    }
}

class ChangedLog(
    username: String,
    affectedId: UUID,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    val changedFrom: String,
    val changedTo: String,
) : Log(username, affectedId, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.CHANGED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId from $changedFrom to $changedTo at $dateTime"
}

class AddedLog(
    username: String,
    affectedId: UUID,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    val addedTo: UUID,
) : Log(username, affectedId, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.ADDED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId to $addedTo at $dateTime"
}

class DeletedLog(
    username: String,
    affectedId: UUID,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    val deletedFrom: String? = null,
) : Log(username, affectedId, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.DELETED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId ${if (!deletedFrom.isNullOrBlank()) "from $deletedFrom" else ""} at $dateTime"
}

class CreatedLog(
    username: String,
    affectedId: UUID,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
) : Log(username, affectedId, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.CREATED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId at $dateTime"
}