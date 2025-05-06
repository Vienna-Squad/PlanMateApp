package org.example.data.datasource.remote.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.UuidRepresentation

import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

object MongoConfig {
    private const val CONNECTION_STRING = "mongodb+srv://mohamedessampd:mFacTfNc0ggBD7Rr@cluster0.qycv0.mongodb.net/sample_mflix?retryWrites=true&w=majority"
    private const val DATABASE_NAME = "mates_hq_db"
    val client: MongoClient by lazy {
        val pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder().automatic(true).build()
        )

        val codecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            pojoCodecRegistry
        )

        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(CONNECTION_STRING))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .codecRegistry(codecRegistry)
            .build()

        MongoClients.create(settings)
    }
    val database: MongoDatabase by lazy {
        client.getDatabase(DATABASE_NAME)
    }
}