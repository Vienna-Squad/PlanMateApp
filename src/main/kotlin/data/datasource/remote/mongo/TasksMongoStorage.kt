package org.example.data.datasource.remote.mongo


import org.bson.Document
import org.example.common.Constants.MongoCollections.TASKS_COLLECTION
import org.example.domain.entity.Task
import java.time.LocalDateTime
import java.util.*


class TasksMongoStorage : MongoStorage<Task>(MongoConfig.database.getCollection(TASKS_COLLECTION)) {
    override fun toDocument(item: Task): Document {
        return Document()
            .append("_id", item.id)
            .append("title", item.title)
            .append("state", item.state)
            .append("assignedTo", item.assignedTo.map { it.toString() })
            .append("createdBy", item.createdBy)
            .append("createdAt", item.createdAt.toString())
            .append("projectId", item.projectId)
    }
    override fun fromDocument(document: Document): Task {
        val assignedToStrings = document.getList("assignedTo", String::class.java) ?: emptyList()
        val assignedTo = assignedToStrings.map { UUID.fromString(it) }
        val uuidStr = document.getString("uuid") ?: document.getString("_id")

        return Task(
            id = UUID.fromString(uuidStr),
            title = document.get("title", String::class.java),
            state = document.get("state", String::class.java),
            assignedTo = assignedTo,
            createdBy = document.get("createdBy", UUID::class.java),
            createdAt = LocalDateTime.parse(document.get("createdAt", String::class.java)),
            projectId = document.get("projectId", UUID::class.java)
        )
    }
}