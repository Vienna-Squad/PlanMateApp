package org.example.data.datasource.local

interface LocalDataSource<T> {
    fun getAll(): List<T>
    fun add(newItem: T)
    fun delete(item: T)
    fun update(updatedItem: T)
}