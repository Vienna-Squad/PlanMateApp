package org.example.data.datasource.remote.mongo.parser

import org.bson.Document
import org.example.data.utils.Parser
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.ChangedLog
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.entity.log.Log.ActionType
import org.example.domain.entity.log.Log.AffectedType
import java.time.LocalDateTime
import java.util.UUID

class LogMongoParser : Parser<Document, Log> {
    override fun serialize(item: Log): Document {
        val doc = Document()
            .append("username", item.username)
            .append("affectedId", item.affectedId.toString())
            .append("affectedName", item.affectedName)
            .append("affectedType", item.affectedType.name)
            .append("dateTime", item.dateTime.toString())

        when (item) {
            is AddedLog -> {
                doc.append("actionType", ActionType.ADDED.name)
                    .append("addedTo", item.addedTo)
            }

            is ChangedLog -> {
                doc.append("actionType", ActionType.CHANGED.name)
                    .append("changedFrom", item.changedFrom)
                    .append("changedTo", item.changedTo)
            }

            is CreatedLog -> {
                doc.append("actionType", ActionType.CREATED.name)
            }

            is DeletedLog -> {
                doc.append("actionType", ActionType.DELETED.name)
                    .append("deletedFrom", item.deletedFrom)
            }
        }

        return doc
    }

    override fun deserialize(item: Document): Log {
        val actionType = ActionType.valueOf(item.get("actionType", String::class.java))
        val username = item.get("username", String::class.java)
        val affectedId = UUID.fromString(item.get("affectedId", String::class.java))
        val affectedName = item.get("affectedName", String::class.java)
        val affectedType = AffectedType.valueOf(item.get("affectedType", String::class.java))
        val dateTime = LocalDateTime.parse(item.get("dateTime", String::class.java))

        return when (actionType) {
            ActionType.ADDED -> AddedLog(
                username = username,
                affectedId = affectedId,
                affectedName = affectedName,
                affectedType = affectedType,
                dateTime = dateTime,
                addedTo = item.get("addedTo", String::class.java)
            )

            ActionType.CHANGED -> ChangedLog(
                username = username,
                affectedId = affectedId,
                affectedName = affectedName,
                affectedType = affectedType,
                dateTime = dateTime,
                changedFrom = item.get("changedFrom", String::class.java),
                changedTo = item.get("changedTo", String::class.java)
            )

            ActionType.CREATED -> CreatedLog(
                username = username,
                affectedId = affectedId,
                affectedName = affectedName,
                affectedType = affectedType,
                dateTime = dateTime
            )

            ActionType.DELETED -> DeletedLog(
                username = username,
                affectedId = affectedId,
                affectedName = affectedName,
                affectedType = affectedType,
                dateTime = dateTime,
                deletedFrom = item.get("deletedFrom", String::class.java)
            )
        }
    }
}