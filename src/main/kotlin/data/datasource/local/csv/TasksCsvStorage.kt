package org.example.data.datasource.local.csv

import org.example.data.bases.EditableCsvStorage
import org.example.domain.NotFoundException
import org.example.domain.entity.Task
import java.io.File
import java.time.LocalDateTime
import java.util.*

class TasksCsvStorage(file: File) : EditableCsvStorage<Task>(file) {

    init {
        writeHeader(getHeaderString())
    }

    override fun toCsvRow(item: Task): String {
        val assignedTo = item.assignedTo.joinToString("|")
        return "${item.id},${item.title},${item.state},${assignedTo},${item.createdBy},${item.projectId},${item.createdAt}\n"
    }

    override fun fromCsvRow(fields: List<String>): Task {
        require(fields.size == EXPECTED_COLUMNS) { "Invalid task data format: " }
        val assignedTo =
            if (fields[ASSIGNED_TO_INDEX].isNotEmpty()) fields[ASSIGNED_TO_INDEX].split(MULTI_VALUE_SEPARATOR).map { UUID.fromString(it) } else emptyList()
        val task = Task(
            id = UUID.fromString(fields[ID_INDEX]),
            title = fields[TITLE_INDEX],
            state = fields[STATE_INDEX],
            assignedTo = assignedTo,
            createdBy = UUID.fromString(fields[CREATED_BY_INDEX]),
            projectId = UUID.fromString(fields[PROJECT_ID_INDEX]),
            createdAt = LocalDateTime.parse(fields[CREATED_AT_INDEX])
        )
        return task
    }

    override fun getHeaderString(): String {
        return CSV_HEADER
    }

    override fun updateItem(item: Task) {
        if (!file.exists()) throw NotFoundException("file")
        val list = read().toMutableList()
        val itemIndex = list.indexOfFirst { it.id == item.id }
        if (itemIndex == -1) throw NotFoundException("$item")
        list[itemIndex] = item
        write(list)
    }

    override fun deleteItem(item: Task) {
        if (!file.exists()) throw NotFoundException("file")
        val list = read().toMutableList()
        val itemIndex = list.indexOfFirst { it.id == item.id }
        if (itemIndex == -1) throw NotFoundException("$item")
        list.removeAt(itemIndex)
        write(list)
    }

    companion object {
        const val CSV_HEADER = "id,title,state,assignedTo,createdBy,projectId,createdAt\n"
        private const val ID_INDEX = 0
        private const val TITLE_INDEX = 1
        private const val STATE_INDEX = 2
        private const val ASSIGNED_TO_INDEX = 3
        private const val CREATED_BY_INDEX = 4
        private const val PROJECT_ID_INDEX = 5
        private const val CREATED_AT_INDEX = 6
        private const val EXPECTED_COLUMNS = 7
        private const val MULTI_VALUE_SEPARATOR = "|"
    }
}