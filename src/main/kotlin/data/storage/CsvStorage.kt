package org.example.data.storage

import data.storage.Storage

abstract class CsvStorage<T>(private val filePath: String) : Storage<T> {
    init {
        createFileIfNotExists()
    }

    private fun createFileIfNotExists() {}

    protected abstract fun writeHeader()

    protected abstract fun serialize(item: T): String
    protected abstract fun deserialize(line: String): T

    override fun write(list: List<T>) {
        TODO("Not yet implemented")
    }

    override fun read(): List<T> {
        TODO("Not yet implemented")
    }

    override fun append(item: T) {
        TODO("Not yet implemented")
    }
}