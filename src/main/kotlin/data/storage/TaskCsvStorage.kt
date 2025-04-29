package org.example.data.storage

import data.storage.CsvStorage
import org.example.domain.entity.Task

class TaskCsvStorage(filePath: String) : CsvStorage<Task>(filePath) {
    override fun writeHeader() {
        TODO("Not yet implemented")
    }

    override fun serialize(item: Task): String {
        TODO("Not yet implemented")
    }

    override fun deserialize(line: String): Task {
        TODO("Not yet implemented")
    }
}