package org.example.data.storage

import data.storage.CsvStorage
import org.example.domain.entity.Task
import java.time.LocalDateTime

class TaskCsvStorage(filePath: String) : CsvStorage<Task>(filePath) {

    companion object {
        private const val ID_INDEX = 0
        private const val TITLE_INDEX = 1
        private const val STATE_INDEX = 2
        private const val ASSIGNED_TO_INDEX = 3
        private const val CREATED_BY_INDEX = 4
        private const val PROJECT_ID_INDEX = 5
        private const val CREATED_AT_INDEX = 6

        private const val EXPECTED_COLUMNS = 7

        private const val CSV_HEADER = "id,title,state,assignedTo,createdBy,projectId,createdAt"

        private const val MULTI_VALUE_SEPARATOR = "|"
    }

    override fun writeHeader() {
        writeToFile("$CSV_HEADER\n")
    }

    override fun serialize(item: Task): String {
        val assignedTo = item.assignedTo.joinToString(MULTI_VALUE_SEPARATOR)
        return "${item.id},${item.title},${item.state},${assignedTo},${item.createdBy},${item.projectId},${item.createdAt}"
    }

    override fun deserialize(line: String): Task {
        val parts = line.split(",", limit = EXPECTED_COLUMNS)
        require(parts.size == EXPECTED_COLUMNS) { "Invalid task data format: $line" }

        val assignedTo = if (parts[ASSIGNED_TO_INDEX].isNotEmpty())
            parts[ASSIGNED_TO_INDEX].split(MULTI_VALUE_SEPARATOR)
        else emptyList()

        return Task(
            id = parts[ID_INDEX],
            title = parts[TITLE_INDEX],
            state = parts[STATE_INDEX],
            assignedTo = assignedTo,
            createdBy = parts[CREATED_BY_INDEX],
            projectId = parts[PROJECT_ID_INDEX],
            createdAt = LocalDateTime.parse(parts[CREATED_AT_INDEX])
        )
    }
}