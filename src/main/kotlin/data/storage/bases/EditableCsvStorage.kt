package org.example.data.storage.bases

import java.io.File

abstract class EditableCsvStorage<T>(file: File) : CsvStorage<T>(file), EditableStorage<T>  {
    override fun write(list: List<T>) {
        file.bufferedWriter().use { writer ->
            writer.write(getHeaderString())
            list.forEach { item ->
                writer.write(toCsvRow(item))
            }
        }
    }
}