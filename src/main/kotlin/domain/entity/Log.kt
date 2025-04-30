package org.example.domain.entity

import java.time.LocalDateTime

//user abc changed task/project XYZ-001 from InProgress to InDevReview at 2025/05/24 8:00 PM
//[ActionType,username, affectedId, affectedType, dateTime,changedFrom, changedTo]
abstract class Log(
    protected val username: String,
    val affectedId: String,
    protected val affectedType: AffectedType,
    protected val dateTime: LocalDateTime = LocalDateTime.now()
) {
    open fun toCsvRow() = listOf(username, affectedId, affectedType.name, dateTime.toString())
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

    companion object {
        const val ACTION_TYPE_INDEX = 0
        const val USERNAME_INDEX = 1
        const val AFFECTED_ID_INDEX = 2
        const val AFFECTED_TYPE_INDEX = 3
        const val DATE_TIME_INDEX = 4
        const val FROM_INDEX = 5
        const val TO_INDEX = 6
    }
}

class ChangedLog(
    username: String,
    affectedId: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    private val changedFrom: String,
    private val changedTo: String,
) : Log(username, affectedId, affectedType, dateTime) {
    constructor(fields: List<String>) : this(
        username = fields[USERNAME_INDEX],
        affectedId = fields[AFFECTED_ID_INDEX],
        affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
        dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
        changedFrom = fields[FROM_INDEX],
        changedTo = fields[TO_INDEX]
    )


    override fun toString(): String {
        return "user $username ${ActionType.CHANGED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId from $changedFrom to $changedTo at $dateTime"
    }

    override fun toCsvRow() = listOf(ActionType.CHANGED.name) + super.toCsvRow() + listOf(changedFrom, changedTo)
}

class AddedLog(
    username: String,
    affectedId: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    private val addedTo: String,
) : Log(username, affectedId, affectedType, dateTime) {
    constructor(fields: List<String>) : this(
        username = fields[USERNAME_INDEX],
        affectedId = fields[AFFECTED_ID_INDEX],
        affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
        dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
        addedTo = fields[TO_INDEX],
    )

    override fun toString() =
        "user $username ${ActionType.ADDED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId to $addedTo at $dateTime"

    override fun toCsvRow() = listOf(ActionType.ADDED.name) + super.toCsvRow() + listOf("", addedTo)
}

class DeletedLog(
    username: String,
    affectedId: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
    private val deletedFrom: String? = null,
) : Log(username, affectedId, affectedType, dateTime) {
    constructor(fields: List<String>) : this(
        username = fields[USERNAME_INDEX],
        affectedId = fields[AFFECTED_ID_INDEX],
        affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
        dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
        deletedFrom = fields.getOrNull(FROM_INDEX),
    )

    override fun toString() =
        "user $username ${ActionType.DELETED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId ${if (deletedFrom != null) "from $deletedFrom" else ""} at $dateTime"

    override fun toCsvRow() = listOf(ActionType.DELETED.name) + super.toCsvRow() + listOf(deletedFrom ?: "", "")
}

class CreatedLog(
    username: String,
    affectedId: String,
    affectedType: AffectedType,
    dateTime: LocalDateTime = LocalDateTime.now(),
) : Log(username, affectedId, affectedType, dateTime) {
    constructor(fields: List<String>) : this(
        username = fields[USERNAME_INDEX],
        affectedId = fields[AFFECTED_ID_INDEX],
        affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
        dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
    )

    override fun toString() =
        "user $username ${ActionType.CREATED.name.lowercase()} ${affectedType.name.lowercase()} $affectedId at $dateTime"

    override fun toCsvRow() = listOf(ActionType.CREATED.name) + super.toCsvRow() + listOf("", "")
}