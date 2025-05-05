package org.example.data.datasource.local.csv

import org.example.data.datasource.local.LocalDataSource
import java.io.File
import java.io.FileNotFoundException

abstract class CsvStorage<T>(val file: File) : LocalDataSource<T> {
    abstract fun toCsvRow(item: T): String
    abstract fun fromCsvRow(fields: List<String>): T
    override fun getAll(): List<T> {
        if (!file.exists()) throw FileNotFoundException()
        val lines = file.readLines()

        if (lines.isEmpty() || lines[0] != getHeaderString().trim()) {
            throw IllegalArgumentException("Invalid CSV format: missing or incorrect header")
        }

        return if (lines.size > 1) {
            lines.drop(1)  // Skip header
                .filter { it.isNotEmpty() }
                .map { row -> fromCsvRow(row.split(",")) }
        } else {
            emptyList()
        }
    }
    override fun add(item: T) {
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