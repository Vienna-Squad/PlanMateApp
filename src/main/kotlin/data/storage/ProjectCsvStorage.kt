package org.example.data.storage

import data.storage.CsvStorage
import org.example.domain.entity.Project
import java.time.LocalDateTime

class ProjectCsvStorage(filePath: String) : CsvStorage<Project>(filePath) {

    companion object {
        private const val ID_INDEX = 0
        private const val NAME_INDEX = 1
        private const val STATES_INDEX = 2
        private const val CREATED_BY_INDEX = 3
        private const val MATES_IDS_INDEX = 4
        private const val CREATED_AT_INDEX = 5

        private const val EXPECTED_COLUMNS = 6

        private const val CSV_HEADER = "id,name,states,createdBy,matesIds,createdAt"

        private const val MULTI_VALUE_SEPARATOR = "|"
    }

    override fun writeHeader() {
        writeToFile("$CSV_HEADER\n")
    }

    override fun serialize(item: Project): String {
        val states = item.states.joinToString(MULTI_VALUE_SEPARATOR)
        val matesIds = item.matesIds.joinToString(MULTI_VALUE_SEPARATOR)
        return "${item.id},${item.name},${states},${item.createdBy},${matesIds},${item.cratedAt}"
    }

    override fun deserialize(line: String): Project {
        val parts = line.split(",", limit = EXPECTED_COLUMNS)
        require(parts.size == EXPECTED_COLUMNS) { "Invalid project data format: $line" }

        val states = if (parts[STATES_INDEX].isNotEmpty())
            parts[STATES_INDEX].split(MULTI_VALUE_SEPARATOR)
        else emptyList()

        val matesIds = if (parts[MATES_IDS_INDEX].isNotEmpty())
            parts[MATES_IDS_INDEX].split(MULTI_VALUE_SEPARATOR)
        else emptyList()

        return Project(
            id = parts[ID_INDEX],
            name = parts[NAME_INDEX],
            states = states,
            createdBy = parts[CREATED_BY_INDEX],
            matesIds = matesIds,
            cratedAt = LocalDateTime.parse(parts[CREATED_AT_INDEX])
        )
    }
}