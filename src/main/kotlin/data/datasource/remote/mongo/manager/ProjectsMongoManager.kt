package org.example.data.datasource.remote.mongo.manager

import org.bson.Document
import org.example.data.datasource.remote.mongo.manager.base.MongoManager
import org.example.data.utils.Parser
import org.example.domain.entity.Project

class ProjectsMongoManager(
    collectionPath: String,
    parser: Parser<Document, Project>
) : MongoManager<Project>(collectionPath, parser) {
    override fun getItemId(item: Project) = item.id
}