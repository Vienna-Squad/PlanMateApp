package org.example.data.datasource.remote.mongo.parser

import org.bson.Document
import org.example.data.utils.Parser
import org.example.domain.entity.Project
import org.example.domain.entity.State
import java.time.LocalDateTime
import java.util.UUID

class ProjectMongoParser : Parser<Document, Project> {
    override fun serialize(item: Project): Document {
        return Document()
            .append("_id", item.id.toString())
            .append("name", item.name)
            .append("states", item.states.map { it.toString() })
            .append("createdBy", item.createdBy.toString())
            .append("createdAt", item.createdAt.toString())
            .append("matesIds", item.matesIds.map { it.toString() })
    }

    override fun deserialize(item: Document): Project {
        val states = item.getList("states", String::class.java).map {
            it.split(":").let { state ->
                State(UUID.fromString(state[0]), state[1])
            }
        }
        val matesIdsStrings = item.getList("matesIds", String::class.java) ?: emptyList()
        val matesIds = matesIdsStrings.map { UUID.fromString(it) }
        val uuidStr = item.getString("_id")
        val createdByStr = item.getString("createdBy")
        return Project(
            id = UUID.fromString(uuidStr),
            name = item.getString("name"),
            states = states,
            createdBy = UUID.fromString(createdByStr),
            createdAt = LocalDateTime.parse(item.getString("createdAt")),
            matesIds = matesIds
        )
    }
}