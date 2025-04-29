package org.example.domain.entity

import java.time.LocalDateTime

//user abc changed task/project XYZ-001 from InProgress to InDevReview at 2025/05/24 8:00 PM
abstract class Log(
    val id: String,
    val username: String,
) {
    val dateTime: LocalDateTime = LocalDateTime.now()

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
    private val affectedType: AffectedType,
    private val oldValue: String,
    private val newValue: String,

    ) : Log(affectedId, username) {
    override fun toString(): String {
        return "user $username changed ${affectedType.name.lowercase()} $id from $oldValue to $newValue at $dateTime"
    }
}

class AddedLog(
    username: String,
    affectedId: String,
    private val affectedType: AffectedType,
    private val addedTo: String,
) : Log(affectedId, username) {
    override fun toString(): String {
        return "user $username added ${affectedType.name.lowercase()} $id to $addedTo at $dateTime"
    }
}

class DeletedLog(
    username: String,
    affectedId: String,
    private val affectedType: AffectedType,
    private val deletedFrom: String? = null,
) : Log(affectedId, username) {
    override fun toString(): String {
        val fromTerm = if (deletedFrom != null) "from $deletedFrom" else ""
        return "user $username deleted ${affectedType.name.lowercase()} $id $fromTerm} at $dateTime"
    }
}

class CreatedLog(
    username: String,
    affectedId: String,
    private val affectedType: AffectedType,
) : Log(affectedId, username) {
    override fun toString(): String {
        return "user $username created ${affectedType.name.lowercase()} $id} at $dateTime"
    }
}