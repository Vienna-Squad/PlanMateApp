package org.example.data.datasource.local.csv.manager.base

import org.example.data.utils.Parser
import java.io.File

abstract class UnEditableCsvFileManager<T>(
    filePath: String,
    private val parser: Parser<String, T>
) {
    protected val file = File(filePath)
    fun readAll(): List<T> {
        if (!file.exists()) file.createNewFile()
        return file.readLines().map { row -> parser.deserialize(row) }
    }

    fun append(newItem: T) {
        if (!file.exists()) file.createNewFile()
        file.appendText(parser.serialize(newItem))
    }
}