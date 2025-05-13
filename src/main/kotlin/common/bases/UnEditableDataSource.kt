package org.example.common.bases

interface UnEditableDataSource<T> {
    fun getAllItems(): List<T>
    fun addItem(newItem: T)
}