package org.example.data.storage

import data.storage.Storage
import org.example.domain.entity.*
import java.io.File
import java.io.FileNotFoundException
import java.text.ParseException

class LogsCsvStorage(private val file: File = File(FILE_NAME)) : Storage<Log> {
    //[ActionType,username, affectedId, affectedType, dateTime,changedFrom, changedTo]
    override fun read(): List<Log> {
        if (!file.exists()) throw FileNotFoundException()
        return file.readLines().map { row -> parseToLog(row.split(",")) }
    }

    override fun append(item: Log) {
        if (!file.exists()) file.createNewFile()
        file.appendText(item.toCsvRow().joinToString(",") + "\n")
    }

    private fun parseToLog(fields: List<String>): Log {
        //[ActionType,username, affectedId, affectedType, dateTime,changedFrom, changedTo]
        if (fields.size != 7) throw ParseException("wrong size of fields it is: ${fields.size}", 0)
        val actionType = Log.ActionType.entries.firstOrNull { it.name == fields.first() } ?: throw ParseException(
            fields.first(),
            0
        )
        return when (actionType) {
            Log.ActionType.CHANGED -> ChangedLog(fields)
            Log.ActionType.ADDED -> AddedLog(fields)
            Log.ActionType.DELETED -> DeletedLog(fields)
            Log.ActionType.CREATED -> CreatedLog(fields)
        }
    }

    companion object {
        const val FILE_NAME = "logs.csv"
    }
}