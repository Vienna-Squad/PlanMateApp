package org.example.domain.entity

import java.time.LocalDateTime

//user abc changed task/project XYZ-001 from InProgress to InDevReview at 2025/05/24 8:00 PM
//[ActionType,username, affectedId, affectedType, dateTime,changedFrom, changedTo]
sealed class Log(
    val username: String,
    val affectedId: String,
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
    affectedId: String,
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
    affectedId: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    val addedTo: String,
) : Log(username, affectedId, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.ADDED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId to $addedTo at $dateTime"
}

class DeletedLog(
    username: String,
    affectedId: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    val deletedFrom: String? = null,
) : Log(username, affectedId, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.DELETED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId ${if (deletedFrom != null) "from $deletedFrom" else ""} at $dateTime"
}

class CreatedLog(
    username: String,
    affectedId: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
) : Log(username, affectedId, affectedType, dateTime) {
    override fun toString() =
        "user $username ${ActionType.CREATED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId at $dateTime"
}