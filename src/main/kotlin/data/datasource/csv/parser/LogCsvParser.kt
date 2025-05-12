package org.example.data.datasource.csv.parser

import org.example.common.bases.CsvParser
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.ChangedLog
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.entity.log.Log.ActionType
import org.example.domain.entity.log.Log.AffectedType
import java.time.LocalDateTime
import java.util.UUID

class LogCsvParser : CsvParser<Log> {
    override fun toCsvRow(item: Log): String {
        return when (item) {
            is AddedLog -> listOf(
                ActionType.ADDED.name,
                item.username,
                item.affectedId,
                item.affectedName,
                item.affectedType,
                item.dateTime,
                "",
                item.addedTo
            )

            is ChangedLog -> listOf(
                ActionType.CHANGED.name,
                item.username,
                item.affectedId,
                item.affectedName,
                item.affectedType,
                item.dateTime,
                item.changedFrom,
                item.changedTo
            )

            is CreatedLog -> listOf(
                ActionType.CREATED.name,
                item.username,
                item.affectedId,
                item.affectedName,
                item.affectedType,
                item.dateTime,
                "",
                ""
            )

            is DeletedLog -> listOf(
                ActionType.DELETED.name,
                item.username,
                item.affectedId,
                item.affectedName,
                item.affectedType,
                item.dateTime,
                item.deletedFrom ?: "",
                ""
            )
        }.joinToString(",") + "\n"
    }

    override fun fromCsvRow(fields: List<String>): Log {
        if (fields.size != EXPECTED_COLUMNS) {
            throw IllegalArgumentException("Invalid CSV format: wrong size of fields, expected $EXPECTED_COLUMNS but got ${fields.size}")
        }

        val actionType =
            ActionType.entries.firstOrNull { it.name == fields[ACTION_TYPE_INDEX] }
                ?: throw IllegalArgumentException("Invalid action type: ${fields[ACTION_TYPE_INDEX]}")

        return when (actionType) {
            ActionType.CHANGED -> ChangedLog(
                username = fields[USERNAME_INDEX],
                affectedId = UUID.fromString(fields[AFFECTED_ID_INDEX]),
                affectedName = fields[AFFECTED_NAME_INDEX],
                affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
                dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
                changedFrom = fields[FROM_INDEX],
                changedTo = fields[TO_INDEX]
            )

            ActionType.ADDED -> AddedLog(
                username = fields[USERNAME_INDEX],
                affectedId = UUID.fromString(fields[AFFECTED_ID_INDEX]),
                affectedName = fields[AFFECTED_NAME_INDEX],
                affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
                dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
                addedTo = fields[TO_INDEX]
            )

            ActionType.DELETED -> DeletedLog(
                username = fields[USERNAME_INDEX],
                affectedId = UUID.fromString(fields[AFFECTED_ID_INDEX]),
                affectedName = fields[AFFECTED_NAME_INDEX],
                affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
                dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
                deletedFrom = fields[FROM_INDEX],
            )

            ActionType.CREATED -> CreatedLog(
                username = fields[USERNAME_INDEX],
                affectedId = UUID.fromString(fields[AFFECTED_ID_INDEX]),
                affectedName = fields[AFFECTED_NAME_INDEX],
                affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
                dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
            )
        }
    }

    companion object {
        private const val ACTION_TYPE_INDEX = 0
        private const val USERNAME_INDEX = 1
        private const val AFFECTED_ID_INDEX = 2
        private const val AFFECTED_NAME_INDEX = 3
        private const val AFFECTED_TYPE_INDEX = 4
        private const val DATE_TIME_INDEX = 5
        private const val FROM_INDEX = 6
        private const val TO_INDEX = 7
        private const val EXPECTED_COLUMNS = 8
    }
}