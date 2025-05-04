package org.example.data.bases

import org.example.domain.NotFoundException
import java.io.File
import java.io.FileNotFoundException

abstract class EditableCsvStorage<T>(file: File) : CsvStorage<T>(file), EditableStorage<T> {
    override fun write(list: List<T>) {
        if (!file.exists()) file.createNewFile()
        val str = StringBuilder()
        str.append(getHeaderString())
        list.forEach { item ->
            str.append(toCsvRow(item))
        }
        file.writeText(str.toString())
    }

    abstract fun updateItem(item: T)

    abstract fun deleteItem(item: T)
}