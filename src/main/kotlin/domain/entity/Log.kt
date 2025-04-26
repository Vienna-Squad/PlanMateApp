package org.example.domain.entity

import java.time.LocalDateTime

//user abc changed task XYZ-001 from InProgress to InDevReview at 2025/05/24 8:00 PM
abstract class Log(
    val username: String,
    val action: Action,
) {
    val dateTime: LocalDateTime = LocalDateTime.now()
}

class TaskLog(
    val taskId: Long,
    username: String,
    action: Action,
) : Log(username, action)

class ProjectLog(
    val projectId: Long,
    username: String,
    action: Action,
) : Log(username, action)

enum class Action {
    ADD,
    EDIT,
    DELETE,
}