package org.example.data.datasource.remote.mongo.manager.base

import com.mongodb.client.model.Filters
import org.bson.Document
import org.example.data.utils.Parser
import java.util.*

abstract class MongoManager<T>(
    collectionPath: String,
    parser: Parser<Document, T>
) : UnEditableMongoManager<T>(collectionPath, parser) {
    fun delete(item: T): Boolean {
        val result = collection.deleteOne(Filters.eq("_id", getItemId(item).toString()))
        return result.deletedCount == 0L
    }

    fun update(updatedItem: T): Boolean {
        val result = collection.replaceOne(
            Filters.eq("_id", getItemId(updatedItem).toString()),
            parser.serialize(updatedItem)
        )
        return result.matchedCount != 0L
    }

    open fun getById(id: UUID): T? {
        return collection.find(Filters.eq("_id", id.toString())).firstOrNull()?.let {
            parser.deserialize(it)
        }
    }

    protected abstract fun getItemId(item: T): UUID
}