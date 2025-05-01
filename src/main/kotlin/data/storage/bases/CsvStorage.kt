package org.example.data.storage.bases

import data.storage.bases.Storage
import java.io.File
import java.io.FileNotFoundException

abstract class CsvStorage<T>(val file: File) : Storage<T> {
    abstract fun toCsvRow(item: T): String
    abstract fun fromCsvRow(fields: List<String>): T
    override fun read(): List<T> {
        if (!file.exists()) throw FileNotFoundException()
        return file.readLines().map { row -> fromCsvRow(row.split(",")) }
    }

    override fun append(item: T) {
        if (!file.exists()) file.createNewFile()
        file.appendText(toCsvRow(item))
    }

    fun writeHeader(header: String) {
        if (!file.exists()) file.createNewFile()
        file.appendText(header)
    }
}