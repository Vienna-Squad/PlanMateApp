package data.datasource.mongo

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document
import org.example.common.bases.DataSource
import org.example.domain.NotFoundException
import org.example.domain.UnknownExceptionException
import java.util.*

abstract class MongoStorage<T>(
    protected val collection: MongoCollection<Document>
) : DataSource<T> {

    abstract fun toDocument(item: T): Document

    abstract fun fromDocument(document: Document): T

    override fun getAllItems() = collection.find().map { fromDocument(it) }.toList()

    override fun getItemById(id: UUID): T {
        return collection.find(Filters.eq("_id", id.toString())).firstOrNull()?.let {
            fromDocument(it)
        } ?: throw NotFoundException()
    }

    override fun addItem(newItem: T) {
        collection.insertOne(toDocument(newItem)).let { result ->
            if (!result.wasAcknowledged()) throw UnknownExceptionException()
        }
    }

    override fun deleteItem(item: T) {
        val document = toDocument(item)
        val result = collection.deleteOne(Filters.eq("_id", document.getString("_id")))
        if (result.deletedCount == 0L) {
            throw NotFoundException()
        }
    }

    override fun updateItem(updatedItem: T) {
        val document = toDocument(updatedItem)
        val result = collection.replaceOne(
            Filters.eq("_id", document.getString("_id")),
            document
        )
        if (result.matchedCount == 0L) {
            throw NotFoundException()
        }
    }
}