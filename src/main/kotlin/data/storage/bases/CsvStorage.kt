package org.example.data.storage.bases

import data.storage.bases.Storage
import java.io.File
import java.io.FileNotFoundException

abstract class CsvStorage<T>(val file: File) : Storage<T> {
    abstract fun toCsvRow(item: T): String
    abstract fun fromCsvRow(fields: List<String>): T
    override fun read(): List<T> {
        if (!file.exists()) throw FileNotFoundException()
        val lines = file.readLines()
        return if (lines.size > 1) {
            lines.drop(1)  // Skip header
                .filter { it.isNotEmpty() }
                .map { row -> fromCsvRow(row.split(",")) }
        } else {
            emptyList()
        }
    }

    override fun append(item: T) {
        if (!file.exists()) {
            file.createNewFile()
            writeHeader(getHeaderString())
        }
        file.appendText(toCsvRow(item))
    }

    fun writeHeader(header: String) {
        if (!file.exists()) file.createNewFile()
        if (file.length() == 0L) {
            file.writeText(header)
        }
    }

    protected abstract fun getHeaderString(): String
}