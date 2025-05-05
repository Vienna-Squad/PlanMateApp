package org.example.data.datasource.remote

interface RemoteDataSource<T> {
    fun getAll(): List<T>
    fun getById(): T
    fun add(newItem: T)
    fun delete(item: T)
    fun update(updatedItem: T)
}