package org.example.common.bases

interface Parser<T> {
    fun serialize(item: T): String
    fun deserialize(row: String): T
}