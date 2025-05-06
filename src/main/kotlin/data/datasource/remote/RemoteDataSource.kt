package org.example.data.datasource.remote

import java.util.UUID

interface RemoteDataSource<T> {
    fun getAll(): List<T>
    fun getById(id: UUID): T
    fun add(newItem: T)
    fun delete(item: T)
    fun update(updatedItem: T)
}