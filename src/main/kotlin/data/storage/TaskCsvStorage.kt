package org.example.data.storage

import org.example.domain.entity.Task
import java.io.File
import java.time.LocalDateTime

class TaskCsvStorage(file: File) : CsvStorage<Task>(file) {

    init {
        writeHeader()
    }

    override fun writeHeader() {
        //"id,title,state,assignedTo,createdBy,projectId,createdAt\n"
    }

    override fun toCsvRow(item: Task): String {
        val assignedTo = item.assignedTo.joinToString("|")
        return "${item.id},${item.title},${item.state},${assignedTo},${item.createdBy},${item.projectId},${item.createdAt}"
    }

    override fun fromCsvRow(fields: List<String>): Task {
        require(fields.size == 7) { "Invalid task data format: " }
        val assignedTo = if (fields[3].isNotEmpty()) fields[3].split("|") else emptyList()
        val task = Task(
            id = fields[0],
            title = fields[1],
            state = fields[2],
            assignedTo = assignedTo,
            createdBy = fields[4],
            projectId = fields[5],
            createdAt = LocalDateTime.parse(fields[6])
        )
        return task
    }
}