package org.example.data.storage

import org.example.data.storage.bases.CsvStorage
import org.example.domain.entity.Project
import java.io.File
import java.time.LocalDateTime

class ProjectCsvStorage(file: File) : CsvStorage<Project>(file) {
    init {
        writeHeader(CSV_HEADER)
    }
    override fun toCsvRow(item: Project): String {
        val states = item.states.joinToString("|")
        val matesIds = item.matesIds.joinToString("|")
        return "${item.id},${item.name},${states},${item.createdBy},${matesIds},${item.cratedAt}"
    }
    override fun fromCsvRow(fields: List<String>): Project {
        require(fields.size == 6) { "Invalid project data format: " }

        val states = if (fields[2].isNotEmpty()) fields[2].split("|") else emptyList()
        val matesIds = if (fields[4].isNotEmpty()) fields[4].split("|") else emptyList()

        val project = Project(
            id = fields[0],
            name = fields[1],
            states = states,
            createdBy = fields[3],
            matesIds = matesIds,
            cratedAt = LocalDateTime.parse(fields[5])
        )

        return project
    }
    companion object {
        private const val ID_INDEX = 0
        private const val NAME_INDEX = 1
        private const val STATES_INDEX = 2
        private const val CREATED_BY_INDEX = 3
        private const val MATES_IDS_INDEX = 4
        private const val CREATED_AT_INDEX = 5
        private const val EXPECTED_COLUMNS = 6
        private const val CSV_HEADER = "id,name,states,createdBy,matesIds,createdAt\n"
        private const val MULTI_VALUE_SEPARATOR = "|"
    }
}