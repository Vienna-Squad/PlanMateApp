package org.example.data.storage

import org.example.domain.entity.*
import org.example.domain.entity.Log.ActionType
import org.example.domain.entity.Log.AffectedType
import java.io.File
import java.text.ParseException
import java.time.LocalDateTime

class LogsCsvStorage(file: File = File(FILE_NAME)) : CsvStorage<Log>(file) {
    //[ActionType,username, affectedId, affectedType, dateTime,changedFrom, changedTo]
    init {
        writeHeader()
    }

    override fun writeHeader() {}
    override fun toCsvRow(item: Log): String {
        return when (item) {
            is AddedLog -> listOf(
                ActionType.CHANGED.name,
                item.username,
                item.affectedId,
                item.affectedType,
                item.dateTime,
                "",
                item.addedTo
            )

            is ChangedLog -> listOf(
                ActionType.CHANGED.name,
                item.username,
                item.affectedId,
                item.affectedType,
                item.dateTime,
                item.changedFrom,
                item.changedTo
            )

            is CreatedLog -> listOf(
                ActionType.CREATED.name,
                item.username,
                item.affectedId,
                item.affectedType,
                item.dateTime,
                "",
                ""
            )

            is DeletedLog -> listOf(
                ActionType.DELETED.name,
                item.username,
                item.affectedId,
                item.affectedType,
                item.dateTime,
                item.deletedFrom ?: "",
                ""
            )
        }.joinToString(",") + "\n"
    }

    override fun fromCsvRow(fields: List<String>): Log {
        //[ActionType,username, affectedId, affectedType, dateTime,changedFrom, changedTo]
        if (fields.size != 7) throw ParseException("wrong size of fields it is: ${fields.size}", 0)
        val actionType = ActionType.entries.firstOrNull { it.name == fields.first() } ?: throw ParseException(
            fields.first(),
            0
        )
        return when (actionType) {
            ActionType.CHANGED -> ChangedLog(
                username = fields[USERNAME_INDEX],
                affectedId = fields[AFFECTED_ID_INDEX],
                affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
                dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
                changedFrom = fields[FROM_INDEX],
                changedTo = fields[TO_INDEX]
            )

            ActionType.ADDED -> AddedLog(
                username = fields[USERNAME_INDEX],
                affectedId = fields[AFFECTED_ID_INDEX],
                affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
                dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
                addedTo = fields[TO_INDEX]
            )

            ActionType.DELETED -> DeletedLog(
                username = fields[USERNAME_INDEX],
                affectedId = fields[AFFECTED_ID_INDEX],
                affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
                dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
                deletedFrom = fields[FROM_INDEX],
            )

            ActionType.CREATED -> CreatedLog(
                username = fields[USERNAME_INDEX],
                affectedId = fields[AFFECTED_ID_INDEX],
                affectedType = AffectedType.valueOf(fields[AFFECTED_TYPE_INDEX]),
                dateTime = LocalDateTime.parse(fields[DATE_TIME_INDEX]),
            )
        }
    }

    companion object {
        const val FILE_NAME = "logs.csv"
        const val USERNAME_INDEX = 1
        const val AFFECTED_ID_INDEX = 2
        const val AFFECTED_TYPE_INDEX = 3
        const val DATE_TIME_INDEX = 4
        const val FROM_INDEX = 5
        const val TO_INDEX = 6
    }
}