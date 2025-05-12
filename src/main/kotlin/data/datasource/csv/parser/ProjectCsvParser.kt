package org.example.data.datasource.csv.parser

import org.example.common.bases.CsvParser
import org.example.domain.entity.Project
import org.example.domain.entity.State
import java.time.LocalDateTime
import java.util.*

class ProjectCsvParser : CsvParser<Project> {
    override fun toCsvRow(item: Project): String {
        val states = item.states.joinToString("|")
        val matesIds = item.matesIds.joinToString("|")
        return "${item.id},${item.name},${states},${item.createdBy},${matesIds},${item.createdAt}\n"
    }

    override fun fromCsvRow(fields: List<String>): Project {
        require(fields.size == EXPECTED_COLUMNS) { "Invalid project data format: " }

        val states =
            if (fields[STATES_INDEX].isNotEmpty()) fields[STATES_INDEX].split(MULTI_VALUE_SEPARATOR)
                .map {
                    it.split(STATE_SEPARATOR).let { state -> State(UUID.fromString(state[0]), state[1]) }
                } else emptyList()
        val matesIds = if (fields[MATES_IDS_INDEX].isNotEmpty()) fields[MATES_IDS_INDEX].split("|") else emptyList()

        val project = Project(
            id = UUID.fromString(fields[ID_INDEX]),
            name = fields[NAME_INDEX],
            states = states,
            createdBy = UUID.fromString(fields[CREATED_BY_INDEX]),
            matesIds = matesIds.map(UUID::fromString),
            createdAt = LocalDateTime.parse(fields[CREATED_AT_INDEX])
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
        private const val MULTI_VALUE_SEPARATOR = "|"
        private const val STATE_SEPARATOR = ":"
    }
}