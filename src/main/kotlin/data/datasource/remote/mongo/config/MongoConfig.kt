package org.example.data.datasource.remote.mongo.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.UuidRepresentation
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.example.BuildConfig

object MongoConfig {
    private const val DATABASE_NAME = BuildConfig.DATABASE_NAME
    private const val CONNECTION_STRING = BuildConfig.MONGO_URI

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