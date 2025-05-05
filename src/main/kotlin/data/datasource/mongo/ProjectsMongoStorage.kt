package org.example.data.datasource.mongo


import com.mongodb.client.model.Filters
import org.bson.Document
import org.example.domain.entity.Project
import java.time.LocalDateTime
import java.util.*

class ProjectsMongoStorage : MongoStorage<Project>(MongoConfig.database.getCollection("Project")) {

    override fun toDocument(item: Project): Document {
        return Document()
            .append("_id", item.id.toString())
            .append("uuid", item.id.toString())
            .append("name", item.name)
            .append("states", item.states)
            .append("createdBy", item.createdBy.toString())
            .append("createdAt", item.cratedAt.toString())
            .append("matesIds", item.matesIds.map { it.toString() })
    }

    override fun fromDocument(document: Document): Project {
        val states = document.getList("states", String::class.java) ?: emptyList()
        val matesIdsStrings = document.getList("matesIds", String::class.java) ?: emptyList()
        val matesIds = matesIdsStrings.map { UUID.fromString(it) }

        val uuidStr = document.getString("uuid") ?: document.getString("_id")
        val createdByStr = document.getString("createdBy")

        return Project(
            id = UUID.fromString(uuidStr),
            name = document.getString("name"),
            states = states,
            createdBy = UUID.fromString(createdByStr),
            cratedAt = LocalDateTime.parse(document.getString("createdAt")),
            matesIds = matesIds
        )
    }

    fun findByCreatedBy(userId: UUID): List<Project> {
        return collection.find(Filters.eq("createdBy", userId))
            .map { fromDocument(it) }
            .toList()
    }

    fun findByProjectId(projectId: UUID): Project? {
        val document = collection.find(Filters.eq("_id", projectId)).first()
        return document?.let { fromDocument(it) }
    }
}