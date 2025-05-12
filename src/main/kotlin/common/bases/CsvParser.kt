package org.example.common.bases

interface CsvParser<T> {
    fun toCsvRow(item: T): String
    fun fromCsvRow(fields: List<String>): T
}