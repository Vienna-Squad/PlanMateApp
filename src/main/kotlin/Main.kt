package org.example

import com.mongodb.client.model.Filters
import di.appModule
import di.useCasesModule
import org.bson.Document
import org.example.data.config.StorageConfig
import org.example.data.config.StorageType
import org.example.di.dataModule
import org.example.di.repositoryModule
import data.datasource.mongo.MongoConfig
import org.example.common.Constants.MongoCollections.USERS_COLLECTION
import org.example.data.repository.UsersRepositoryImpl
import org.example.domain.entity.User
import org.example.presentation.AuthApp
import org.koin.core.context.GlobalContext.startKoin
import java.time.LocalDateTime
import java.util.UUID

fun main() {
    println("Hello, PlanMate!")

    selectStorageType()

    startKoin { modules(appModule, useCasesModule, repositoryModule, dataModule) }

    createAdminUser()

    AuthApp().run()
}

fun selectStorageType() {
    println("Please select your storage type:")
    println("1. Local Storage (CSV files)")
    println("2. Remote Storage (MongoDB)")

    var validInput = false
    while (!validInput) {
        print("Enter your choice (1 or 2): ")
        when (readLine()?.trim()) {
            "1" -> {
                StorageConfig.currentStorageType = StorageType.LOCAL
                println("Local Storage (CSV) selected.")
                validInput = true
            }
            "2" -> {
                StorageConfig.currentStorageType = StorageType.REMOTE
                println("Remote Storage (MongoDB) selected.")
                validInput = true
            }
            else -> println("Invalid input. Please enter 1 or 2.")
        }
    }
}

fun createAdminUser() {
    println("Creating admin user...")
    try {
        if (StorageConfig.currentStorageType == StorageType.REMOTE) {
            val collection = MongoConfig.database.getCollection(USERS_COLLECTION)

            val existingAdmin = collection.find(Filters.eq("username", "mohannad")).first()
            if (existingAdmin != null) {
                println("Admin user already exists")
                return
            }

            val adminId = UUID.randomUUID()
            val adminDoc = Document()
                .append("_id", adminId.toString())
                .append("username", "mohannad")
                .append("hashedPassword", UsersRepositoryImpl.encryptPassword("12345678"))
                .append("role", User.UserRole.ADMIN.name)
                .append("createdAt", LocalDateTime.now().toString())

            collection.insertOne(adminDoc)
            println("Created admin user: mohannad / 12345678")
        } else {
            println("Note: For local CSV storage, the admin user will be created on first use.")
        }
    } catch (e: Exception) {
        println("Error creating admin: ${e.message}")
        e.printStackTrace()
    }
}