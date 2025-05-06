package org.example.data.datasource.remote.mongo

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document
import org.example.data.datasource.remote.RemoteDataSource
import org.example.domain.NotFoundException

abstract class MongoStorage<T>(
    protected val collection: MongoCollection<Document>
) : RemoteDataSource<T> {

    abstract fun toDocument(item: T): Document
    abstract fun fromDocument(document: Document): T

    override fun getAll(): List<T> {
        return collection.find().map { fromDocument(it) }.toList()
    }

    override fun add(newItem: T) {
        collection.insertOne(toDocument(newItem))
    }

    override fun delete(item: T) {
        val document = toDocument(item)
        val result = collection.deleteOne(Filters.eq("_id", document.getString("_id")))
        if (result.deletedCount == 0L) {
            throw NotFoundException("Item not found")
        }
    }

    override fun update(updatedItem: T) {
        val document = toDocument(updatedItem)
        val result = collection.replaceOne(
            Filters.eq("_id", document.getString("_id")),
            document
        )
        if (result.matchedCount == 0L) {
            throw NotFoundException("Item not found")
        }
    }
}