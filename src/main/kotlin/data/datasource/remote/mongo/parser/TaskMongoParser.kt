package org.example.data.datasource.remote.mongo.parser

import org.bson.Document
import org.example.data.utils.Parser
import org.example.domain.entity.State
import org.example.domain.entity.Task
import java.time.LocalDateTime
import java.util.UUID

class TaskMongoParser : Parser<Document, Task> {
    override fun serialize(item: Task): Document {
        return Document()
            .append("_id", item.id.toString())
            .append("title", item.title)
            .append("state", item.state.toString())
            .append("assignedTo", item.assignedTo.map { it.toString() })
            .append("createdBy", item.createdBy)
            .append("createdAt", item.createdAt.toString())
            .append("projectId", item.projectId)
    }

    override fun deserialize(item: Document): Task {
        val assignedToStrings = item.getList("assignedTo", String::class.java) ?: emptyList()
        val assignedTo = assignedToStrings.map { UUID.fromString(it) }
        val uuidStr = item.getString("_id")
        val state = item.get("state", String::class.java).let {
            it.split(":").let { str -> State(UUID.fromString(str[0]), str[1]) }
        }

        return Task(
            id = UUID.fromString(uuidStr),
            title = item.get("title", String::class.java),
            state = state,
            assignedTo = assignedTo,
            createdBy = item.get("createdBy", UUID::class.java),
            createdAt = LocalDateTime.parse(item.get("createdAt", String::class.java)),
            projectId = item.get("projectId", UUID::class.java)
        )
    }
}