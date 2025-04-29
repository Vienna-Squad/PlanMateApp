package org.example.data.storage

import org.example.domain.entity.Project

class ProjectCsvStorage(filePath: String) : CsvStorage<Project>(filePath) {
    override fun writeHeader() {
        TODO("Not yet implemented")
    }

    override fun serialize(item: Project): String {
        TODO("Not yet implemented")
    }

    override fun deserialize(line: String): Project {
        TODO("Not yet implemented")
    }
}