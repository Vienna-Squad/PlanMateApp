package data.datasource.mongo


import org.bson.Document
import org.example.common.Constants.MongoCollections.TASKS_COLLECTION
import org.example.domain.NotFoundException
import org.example.domain.entity.State
import org.example.domain.entity.Task
import java.time.LocalDateTime
import java.util.*


class TasksMongoStorage : MongoStorage<Task>(MongoConfig.database.getCollection(TASKS_COLLECTION)) {
    override fun toDocument(item: Task): Document {
        return Document()
            .append("_id", item.id.toString())
            .append("title", item.title)
            .append("state", item.state.toString())
            .append("assignedTo", item.assignedTo.map { it.toString() })
            .append("createdBy", item.createdBy)
            .append("createdAt", item.createdAt.toString())
            .append("projectId", item.projectId)
    }

    override fun fromDocument(document: Document): Task {
        val assignedToStrings = document.getList("assignedTo", String::class.java) ?: emptyList()
        val assignedTo = assignedToStrings.map { UUID.fromString(it) }
        val uuidStr = document.getString("_id")
        val state = document.get("state", String::class.java).let {
            it.split(":").let { str -> State(UUID.fromString(str[0]), str[1]) }
        }

        return Task(
            id = UUID.fromString(uuidStr),
            title = document.get("title", String::class.java),
            state = state,
            assignedTo = assignedTo,
            createdBy = document.get("createdBy", UUID::class.java),
            createdAt = LocalDateTime.parse(document.get("createdAt", String::class.java)),
            projectId = document.get("projectId", UUID::class.java)
        )
    }

    override fun getAll() = super.getAll().ifEmpty { throw NotFoundException("tasks") }
}