package org.example.data.datasource.mongo


import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document

class MongoPreferences {
    private val collection = MongoConfig.database.getCollection("preferences")

    fun get(key: String): String? {
        val document = collection.find(Filters.eq("_id", key)).first()
        return document?.getString("value")
    }

    fun put(key: String, value: String) {
        val document = Document()
            .append("_id", key)
            .append("value", value)

        collection.replaceOne(
            Filters.eq("_id", key),
            document,
            ReplaceOptions().upsert(true)
        )
    }

    fun remove(key: String) {
        collection.deleteOne(Filters.eq("_id", key))
    }

    fun clear() {
        collection.deleteMany(Document())
    }
}