package org.example.data.datasource.remote.mongo.manager

import org.bson.Document
import org.example.data.datasource.remote.mongo.manager.base.MongoManager
import org.example.data.utils.Parser
import org.example.domain.entity.Task

class TasksMongoManager(
    collectionPath: String,
    parser: Parser<Document, Task>
) : MongoManager<Task>(collectionPath, parser) {
    override fun getItemId(item: Task) = item.id
}