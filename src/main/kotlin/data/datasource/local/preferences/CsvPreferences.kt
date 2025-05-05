package org.example.data.datasource.local.preferences

import org.example.data.datasource.local.csv.CsvStorage
import java.io.File
import java.io.FileNotFoundException

class CsvPreferences(file: File) : CsvStorage<Pair<String, String>>(file), Preference {
    private val map: MutableMap<String, String> = mutableMapOf()
    override fun get(key: String): String? = map[key]
    override fun put(key: String, value: String) {
        map[key] = value
        add(Pair(key, value))
    }
    override fun remove(key: String) {
        delete(Pair(key, ""))
    }
    override fun clear() {
        map.clear()
        if (!file.exists()) throw FileNotFoundException("file")
        file.writeText("")
    }


    override fun update(updatedItem: Pair<String, String>) {
        map[updatedItem.first] = updatedItem.second
        val listOfPairs = map.map { Pair(it.key, it.value) }.toList()
        write(listOfPairs)
    }

    override fun delete(item: Pair<String, String>) {
        map.remove(item.first)
        val listOfPairs = map.map { Pair(it.key, it.value) }.toList()
        write(listOfPairs)
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
}
