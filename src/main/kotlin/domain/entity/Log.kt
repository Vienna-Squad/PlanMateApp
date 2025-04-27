package org.example.domain.entity

import java.time.LocalDateTime

//user abc changed task/project XYZ-001 from InProgress to InDevReview at 2025/05/24 8:00 PM
class Log(
    val id: String,
    val username: String,
    val action: Action,
) {
    val dateTime: LocalDateTime = LocalDateTime.now()
}

enum class Action {
    ADD,
    EDIT,
    DELETE,
}