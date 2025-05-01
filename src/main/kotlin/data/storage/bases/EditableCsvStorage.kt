package org.example.data.storage.bases

import java.io.File
import java.io.FileNotFoundException

abstract class EditableCsvStorage<T>(file: File) : CsvStorage<T>(file), EditableStorage<T>  {
    override fun write(list: List<T>) {
        if (!file.exists()) throw FileNotFoundException()
        file.bufferedWriter().use { writer ->
            writer.write(getHeaderString())
            list.forEach { item ->
                writer.write(toCsvRow(item))
            }
        }
    }
}