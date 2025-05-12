package org.example.common.bases

import java.io.File

open class UnEditableCsvFileManager<T>(
    filePath: String,
    private val parser: CsvParser<T>
) {
    protected val file = File(filePath)
    fun readAll(): List<T> {
        if (!file.exists()) file.createNewFile()
        return file.readLines().map { row -> parser.fromCsvRow(row.split(",")) }
    }

    fun append(newItem: T) {
        if (!file.exists()) file.createNewFile()
        file.appendText(parser.toCsvRow(newItem))
    }
}