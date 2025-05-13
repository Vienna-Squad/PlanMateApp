package org.example.data.datasource

import java.util.UUID

interface DataSource<T> : UnEditableDataSource<T> {
    fun getItemById(id: UUID): T
    fun deleteItem(item: T)
    fun updateItem(updatedItem: T)
}