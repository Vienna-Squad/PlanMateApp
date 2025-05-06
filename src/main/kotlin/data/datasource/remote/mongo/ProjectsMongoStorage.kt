package org.example.data.datasource.remote.mongo


import org.bson.Document
import org.example.common.Constants.MongoCollections.PROJECTS_COLLECTION
import data.datasource.remote.mongo.MongoConfig
import org.example.domain.entity.Project
import java.time.LocalDateTime
import java.util.*

class ProjectsMongoStorage : MongoStorage<Project>(MongoConfig.database.getCollection(PROJECTS_COLLECTION)) {
    override fun toDocument(item: Project): Document {
        return Document()
            .append("_id", item.id.toString())
            .append("name", item.name)
            .append("states", item.states)
            .append("createdBy", item.createdBy.toString())
            .append("createdAt", item.createdAt.toString())
            .append("matesIds", item.matesIds.map { it.toString() })
    }

    override fun fromDocument(document: Document): Project {
        val states = document.getList("states", String::class.java) ?: emptyList()
        val matesIdsStrings = document.getList("matesIds", String::class.java) ?: emptyList()
        val matesIds = matesIdsStrings.map { UUID.fromString(it) }
        val uuidStr = document.getString("_id")
        val createdByStr = document.getString("createdBy")
        return Project(
            id = UUID.fromString(uuidStr),
            name = document.getString("name"),
            states = states,
            createdBy = UUID.fromString(createdByStr),
            createdAt = LocalDateTime.parse(document.getString("createdAt")),
            matesIds = matesIds
        )
    }
}