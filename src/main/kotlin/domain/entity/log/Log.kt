package org.example.domain.entity.log

import java.time.LocalDateTime
import java.util.*

sealed class Log(
    val username: String,
    val affectedId: UUID,
    val affectedName: String,
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