package org.example.data.storage.bases

import data.storage.bases.Storage

interface EditableStorage<T> : Storage<T> {
    fun write(list: List<T>)
}