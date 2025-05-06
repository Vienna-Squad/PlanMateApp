package org.example

import com.mongodb.client.model.Filters
import common.di.appModule
import common.di.useCasesModule
import org.bson.Document
import org.example.common.di.dataModule
import org.example.common.di.repositoryModule
import org.example.data.datasource.remote.mongo.MongoConfig
import org.example.data.repository.UsersRepositoryImpl
import org.example.domain.entity.UserRole
import org.example.presentation.AuthApp
import org.koin.core.context.GlobalContext.startKoin
import java.time.LocalDateTime
import java.util.UUID

fun main() {
    println("Hello, PlanMate!")
    startKoin { modules(appModule, useCasesModule, repositoryModule, dataModule) }
    createAdminUser()
    AuthApp().run()
}

fun createAdminUser() {
    println("Creating admin user...")
    try {
        val collection = MongoConfig.database.getCollection("User")

        // Check if admin1 already exists
        val existingAdmin = collection.find(Filters.eq("username", "admin1")).first()
        if (existingAdmin != null) {
            println("Admin user already exists")
            return
        }

        // Create a new admin user with string UUID
        val adminId = UUID.randomUUID()
        val adminDoc = Document()
            .append("_id", adminId.toString())
            .append("uuid", adminId.toString())
            .append("username", "admin2")
            .append("hashedPassword", UsersRepositoryImpl.encryptPassword("12345678"))
            .append("role", UserRole.ADMIN.name)
            .append("createdAt", LocalDateTime.now().toString())

        collection.insertOne(adminDoc)
        println("Created admin user: admin2 / 12345678")

    } catch (e: Exception) {
        println("Error creating admin: ${e.message}")
        e.printStackTrace()
    }
}

