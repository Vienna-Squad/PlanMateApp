package org.example.data.datasource.preferences

import org.example.data.bases.EditableCsvStorage
import java.io.File

class CsvPreferences(file: File) : EditableCsvStorage<Pair<String, String>>(file) {
    private val map: MutableMap<String, String> = mutableMapOf()

    fun put(key: String, value: String) {
        updateItem(Pair(key, value))
    }

    fun get(key: String): String? = map[key]

    fun remove(key: String) {
        deleteItem(Pair(key, ""))
    }

    fun clear() {
        map.clear()
        write(emptyList())
    }

    override fun toCsvRow(item: Pair<String, String>): String {
        return "${item.first},${item.second}\n"
    }

    override fun fromCsvRow(fields: List<String>): Pair<String, String> {
        return Pair(fields[0], fields[1])
    }

    override fun getHeaderString(): String {
        return "key,value\n"
    }

    override fun updateItem(item: Pair<String, String>) {
        map[item.first] = item.second
        write(map.map { Pair(it.key, it.value) })
    }

    override fun deleteItem(item: Pair<String, String>) {
        map.remove(item.first)
        write(map.map { Pair(it.key, it.value) })
    }

}
