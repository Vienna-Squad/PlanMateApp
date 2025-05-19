package org.example.data.datasource.remote.mongo.manager.base

import com.mongodb.client.MongoCollection
import org.bson.Document
import org.example.data.datasource.remote.mongo.config.MongoConfig
import org.example.data.utils.Parser

abstract class UnEditableMongoManager<T>(
    collectionPath: String,
    protected val parser: Parser<Document, T>
) {
    protected val collection: MongoCollection<Document> = MongoConfig.database.getCollection(collectionPath)

    open fun readAll() = collection.find().mapNotNull { parser.deserialize(it) }.toList()

    fun append(newItem: T): Boolean {
        collection.insertOne(parser.serialize(newItem)).let { result ->
            return result.wasAcknowledged()
        }
    }
}