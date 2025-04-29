package org.example.data.storage

import data.storage.CsvStorage
import org.example.domain.entity.Log

class LogCsvStorage(filePath: String) : CsvStorage<Log>(filePath) {
    override fun writeHeader() {
        TODO("Not yet implemented")
    }

    override fun serialize(item: Log): String {
        TODO("Not yet implemented")
    }

    override fun deserialize(line: String): Log {
        TODO("Not yet implemented")
    }
}