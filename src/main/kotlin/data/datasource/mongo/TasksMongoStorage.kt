package org.example.data.datasource.mongo


import com.mongodb.client.model.Filters
import org.bson.Document
import org.example.domain.entity.Task
import java.time.LocalDateTime
import java.util.*


class TasksMongoStorage : MongoStorage<Task>(MongoConfig.database.getCollection("Task")) {

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

    fun findByProjectId(projectId: UUID): List<Task> {
        return collection.find(Filters.eq("projectId", projectId))
            .map { fromDocument(it) }
            .toList()
    }

    fun findByTaskId(taskId: UUID): Task? {
        val document = collection.find(Filters.eq("_id", taskId)).first()
        return document?.let { fromDocument(it) }
    }

    fun findByCreatedBy(userId: UUID): List<Task> {
        return collection.find(Filters.eq("createdBy", userId))
            .map { fromDocument(it) }
            .toList()
    }

    fun findByAssignedTo(userId: UUID): List<Task> {
        val result = mutableListOf<Task>()
        collection.find().forEach { document ->
            try {
                val assignedToStrings = document.getList("assignedTo", String::class.java) ?: emptyList()
                if (assignedToStrings.contains(userId.toString())) {
                    result.add(fromDocument(document))
                }
            } catch (e: Exception) {
                println("Error checking task assignment: ${e.message}")
            }
        }

        return result
    }
}