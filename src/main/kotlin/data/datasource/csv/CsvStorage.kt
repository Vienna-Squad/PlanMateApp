package data.datasource.csv

import data.datasource.DataSource
import java.io.File
import java.io.FileNotFoundException

abstract class CsvStorage<T>(val file: File) : DataSource<T> {
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

    override fun add(newItem: T) {
        if (!file.exists()) {
            file.createNewFile()
            file.writeText(getHeaderString())
        }
        file.appendText(toCsvRow(newItem))
    }

    fun write(items: List<T>) {
        if (!file.exists()) {
            file.createNewFile()
            file.writeText(getHeaderString())
        }
        val str = StringBuilder()
        items.forEach {
            str.append(toCsvRow(it))
        }
        file.writeText(str.toString())
    }

    protected abstract fun getHeaderString(): String
}