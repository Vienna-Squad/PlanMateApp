package org.example.data.datasource.local.bases

interface Parser<T> {
    fun serialize(item: T): String
    fun deserialize(row: String): T
}