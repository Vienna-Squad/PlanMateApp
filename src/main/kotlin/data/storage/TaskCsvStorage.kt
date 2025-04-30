package org.example.data.storage

import data.storage.CsvStorage
import org.example.domain.entity.Task
import java.time.LocalDateTime

class TaskCsvStorage(filePath: String) : CsvStorage<Task>(filePath) {

    override fun writeHeader() {
        writeToFile("id,title,state,assignedTo,createdBy,projectId,createdAt\n")
    }

    override fun serialize(item: Task): String {
        val assignedTo = item.assignedTo.joinToString("|")
        return "${item.id},${item.title},${item.state},${assignedTo},${item.createdBy},${item.projectId},${item.createdAt}"
    }

    override fun deserialize(line: String): Task {
        val parts = line.split(",", limit = 7)
        require(parts.size == 7) { "Invalid task data format: $line" }

        val assignedTo = if (parts[3].isNotEmpty()) parts[3].split("|") else emptyList()

        val task = Task(
            id = parts[0],
            title = parts[1],
            state = parts[2],
            assignedTo = assignedTo,
            createdBy = parts[4],
            projectId = parts[5],
            createdAt = LocalDateTime.parse(parts[6])
        )

        return task
    }
}