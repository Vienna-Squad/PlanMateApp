package org.example.data.datasource.local

import java.util.UUID

interface LocalDataSource<T> {
    fun getAll(): List<T>
    fun getById(id: UUID): T
    fun add(newItem: T)
    fun delete(item: T)
    fun update(updatedItem: T)
}