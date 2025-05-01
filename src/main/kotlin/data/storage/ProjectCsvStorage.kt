package org.example.data.storage

import org.example.data.storage.bases.EditableCsvStorage
import org.example.domain.entity.Project
import java.io.File
import java.time.LocalDateTime

class ProjectCsvStorage(file: File) :  EditableCsvStorage<Project>(file) {
    init {
        writeHeader(getHeaderString())
    }
    override fun toCsvRow(item: Project): String {
        val states = item.states.joinToString("|")
        val matesIds = item.matesIds.joinToString("|")
        return "${item.id},${item.name},${states},${item.createdBy},${matesIds},${item.cratedAt}\n"
    }
    override fun fromCsvRow(fields: List<String>): Project {
        require(fields.size == EXPECTED_COLUMNS) { "Invalid project data format: " }

        val states = if (fields[STATES_INDEX].isNotEmpty()) fields[STATES_INDEX].split(MULTI_VALUE_SEPARATOR) else emptyList()
        val matesIds = if (fields[MATES_IDS_INDEX].isNotEmpty()) fields[MATES_IDS_INDEX].split("|") else emptyList()

        val project = Project(
            id = fields[ID_INDEX],
            name = fields[NAME_INDEX],
            states = states,
            createdBy = fields[CREATED_BY_INDEX],
            matesIds = matesIds,
            cratedAt = LocalDateTime.parse(fields[CREATED_AT_INDEX])
        )

        return project
    }

    override fun getHeaderString(): String {
        return CSV_HEADER
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