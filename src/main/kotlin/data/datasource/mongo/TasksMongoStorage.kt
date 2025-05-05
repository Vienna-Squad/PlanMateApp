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
            .append("assignedTo", item.assignedTo)
            .append("createdBy", item.createdBy)
            .append("createdAt", item.createdAt.toString())
            .append("projectId", item.projectId)
    }

    override fun fromDocument(document: Document): Task {
        val assignedTo = document.getList("assignedTo", UUID::class.java) ?: emptyList()

        return Task(
            id = document.get("_id", UUID::class.java),
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
        // Fix: Using the proper syntax for finding tasks where userId is in the assignedTo array
        val tasks = ArrayList<Task>()
        collection.find(Filters.all("assignedTo", listOf(userId))).forEach { document ->
            tasks.add(fromDocument(document))
        }
        return tasks
    }
}