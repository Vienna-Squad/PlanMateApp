package org.example.data.storage

import data.storage.CsvStorage
import org.example.domain.entity.Project
import java.time.LocalDateTime

class ProjectCsvStorage(filePath: String) : CsvStorage<Project>(filePath) {

    override fun writeHeader() {
        writeToFile("id,name,states,createdBy,matesIds,createdAt\n")
    }

    override fun serialize(item: Project): String {
        val states = item.states.joinToString("|")
        val matesIds = item.matesIds.joinToString("|")
        return "${item.id},${item.name},${states},${item.createdBy},${matesIds},${item.cratedAt}"
    }

    override fun deserialize(line: String): Project {
        val parts = line.split(",", limit = 6)
        require(parts.size == 6) { "Invalid project data format: $line" }

        val states = if (parts[2].isNotEmpty()) parts[2].split("|") else emptyList()
        val matesIds = if (parts[4].isNotEmpty()) parts[4].split("|") else emptyList()

        val project = Project(
            name = parts[1],
            states = states,
            createdBy = parts[3],
            matesIds = matesIds
        )

        // Set the ID and createdAt fields
        setPrivateField(project, "id", parts[0])
        setPrivateField(project, "cratedAt", LocalDateTime.parse(parts[5]))

        return project
    }

    private fun setPrivateField(obj: Any, fieldName: String, value: Any) {
        val field = obj.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(obj, value)
        field.isAccessible = false
    }
}