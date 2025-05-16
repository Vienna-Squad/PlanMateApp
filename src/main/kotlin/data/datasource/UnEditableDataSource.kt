package org.example.data.datasource

interface UnEditableDataSource<T> {
    fun getAllItems(): List<T>
    fun addItem(newItem: T)
}