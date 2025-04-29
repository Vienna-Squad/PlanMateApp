package org.example.data.storage

interface EditableStorage<T> {
    fun write(list: List<T>)
}