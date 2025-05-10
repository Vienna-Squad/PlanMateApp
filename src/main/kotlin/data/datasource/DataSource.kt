package data.datasource

import java.util.UUID

interface DataSource<T> {
    fun getAll(): List<T>
    fun getById(id: UUID): T
    fun add(newItem: T)
    fun delete(item: T)
    fun update(updatedItem: T)
}