package org.example.data.datasource.remote.mongo


import org.bson.Document
import org.example.common.Constants.MongoCollections.LOGS_COLLECTION
import org.example.domain.entity.*
import org.example.domain.entity.Log.ActionType
import org.example.domain.entity.Log.AffectedType
import java.time.LocalDateTime
import java.util.*

class LogsMongoStorage : MongoStorage<Log>(MongoConfig.database.getCollection(LOGS_COLLECTION)) {
    override fun toDocument(item: Log): Document {
        val doc = Document()
            .append("username", item.username)
            .append("affectedId", item.affectedId)
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
    override fun fromDocument(document: Document): Log {
        val actionType = ActionType.valueOf(document.get("actionType", String::class.java))
        val username = document.get("username", String::class.java)
        val affectedId = document.get("affectedId", UUID::class.java)
        val affectedType = AffectedType.valueOf(document.get("affectedType", String::class.java))
        val dateTime = LocalDateTime.parse(document.get("dateTime", String::class.java))

        return when (actionType) {
            ActionType.ADDED -> AddedLog(
                username = username,
                affectedId = affectedId,
                affectedType = affectedType,
                dateTime = dateTime,
                addedTo = document.get("addedTo", UUID::class.java)
            )

            ActionType.CHANGED -> ChangedLog(
                username = username,
                affectedId = affectedId,
                affectedType = affectedType,
                dateTime = dateTime,
                changedFrom = document.get("changedFrom", String::class.java),
                changedTo = document.get("changedTo", String::class.java)
            )

            ActionType.CREATED -> CreatedLog(
                username = username,
                affectedId = affectedId,
                affectedType = affectedType,
                dateTime = dateTime
            )

            ActionType.DELETED -> DeletedLog(
                username = username,
                affectedId = affectedId,
                affectedType = affectedType,
                dateTime = dateTime,
                deletedFrom = document.get("deletedFrom", String::class.java)
            )
        }
    }
}