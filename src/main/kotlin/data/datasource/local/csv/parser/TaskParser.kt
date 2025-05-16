package org.example.data.datasource.local.csv.parser

import org.example.data.datasource.local.bases.Parser
import org.example.domain.entity.State
import org.example.domain.entity.Task
import java.time.LocalDateTime
import java.util.UUID

class TaskParser : Parser<Task> {
    override fun serialize(item: Task): String {
        val assignedTo = item.assignedTo.joinToString("|")
        return "${item.id},${item.title},${item.state},${assignedTo},${item.createdBy},${item.projectId},${item.createdAt}\n"
    }

    override fun deserialize(row: String): Task {
        val fields: List<String> = row.split(",")
        require(fields.size == EXPECTED_COLUMNS) { "Invalid task data format: " }
        val assignedTo =
            if (fields[ASSIGNED_TO_INDEX].isNotEmpty()) fields[ASSIGNED_TO_INDEX].split(MULTI_VALUE_SEPARATOR)
                .map { UUID.fromString(it) } else emptyList()
        val task = Task(
            id = UUID.fromString(fields[ID_INDEX]),
            title = fields[TITLE_INDEX],
            state = fields[STATE_INDEX].split(STATE_SEPARATOR).let { State(UUID.fromString(it[0]), it[1]) },
            assignedTo = assignedTo,
            createdBy = UUID.fromString(fields[CREATED_BY_INDEX]),
            projectId = UUID.fromString(fields[PROJECT_ID_INDEX]),
            createdAt = LocalDateTime.parse(fields[CREATED_AT_INDEX])
        )
        return task
    }

    companion object {
        private const val ID_INDEX = 0
        private const val TITLE_INDEX = 1
        private const val STATE_INDEX = 2
        private const val ASSIGNED_TO_INDEX = 3
        private const val CREATED_BY_INDEX = 4
        private const val PROJECT_ID_INDEX = 5
        private const val CREATED_AT_INDEX = 6
        private const val EXPECTED_COLUMNS = 7
        private const val MULTI_VALUE_SEPARATOR = "|"
        private const val STATE_SEPARATOR = ":"
    }
}