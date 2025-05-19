package org.example.data.datasource.local.csv.manager.base

import org.example.data.utils.Parser
import java.util.*

abstract class CsvFileManager<T>(
    filePath: String,
    parser: Parser<String, T>
) : UnEditableCsvFileManager<T>(filePath, parser) {
    fun clear() {
        if (file.exists()) file.writeText("")
    }

    fun delete(item: T): Boolean {
        val items = readAll()
        if (item !in items) return false
        (items - item).let { newItems ->
            clear()
            newItems.forEach { append(it) }
            return true
        }
    }

    fun update(updatedItem: T): Boolean {
        readAll().toMutableList().let { items ->
            val index = items.indexOfFirst { getItemId(it) == getItemId(updatedItem) }
            if (index == -1) return false
            items[index] = updatedItem
            clear()
            items.forEach { append(it) }
            return true
        }
    }

    fun getById(id: UUID) = readAll().find { getItemId(it) == id }
    protected abstract fun getItemId(item: T): UUID
}