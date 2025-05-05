package org.example.data.datasource.local.preferences

interface Preference {
    fun put(key: String, value: String)
    fun get(key: String): String?
    fun remove(key: String)
    fun clear()
}