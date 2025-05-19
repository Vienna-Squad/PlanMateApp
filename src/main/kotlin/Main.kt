package org.example

import com.mongodb.client.model.Filters
import data.datasource.remote.mongo.MongoConfig
import di.appModule
import di.useCasesModule
import org.bson.Document
import org.example.MongoCollections.USERS_COLLECTION
import org.example.data.repository.UsersRepositoryImpl.Companion.encryptPassword
import org.example.di.dataModule
import org.example.di.repositoryModule
import org.example.domain.entity.User
import org.example.domain.repository.UsersRepository
import org.example.presentation.AuthApp
import org.koin.core.context.GlobalContext.startKoin
import org.koin.mp.KoinPlatform.getKoin
import java.time.LocalDateTime
import java.util.*

fun main() {
    println("Hello, PlanMate!")
    startKoin { modules(appModule, useCasesModule, repositoryModule, dataModule) }
    createAdminUser()
    createLocalAdminUser()
    AuthApp().run()
}

fun createAdminUser() {
    println("Creating admin user...")
    try {
        val collection = MongoConfig.database.getCollection(USERS_COLLECTION)

        // Check if admin1 already exists
        val existingAdmin = collection.find(Filters.eq("username", "mohannad")).first()
        if (existingAdmin != null) {
            println("Admin user already exists")
            return
        }

        // Create a new admin user with string UUID
        val adminId = UUID.randomUUID()
        val adminDoc = Document()
            .append("_id", adminId.toString())
            .append("username", "mohannad")
            .append("hashedPassword", encryptPassword("12345678"))
            .append("role", User.UserRole.ADMIN.name)
            .append("createdAt", LocalDateTime.now().toString())

        collection.insertOne(adminDoc)
        println("Created admin user: admin / 12345678")

    } catch (e: Exception) {
        println("Error creating admin: ${e.message}")
        e.printStackTrace()
    }
}

fun createLocalAdminUser() {
    val username = "admin-101"
    val password = "12345"
    println("Creating local admin ...")
    val repo: UsersRepository = getKoin().get()
    repo.createUser(
        User(
            username = username,
            hashedPassword = encryptPassword(password),
            role = User.UserRole.ADMIN
        )
    )
    println("Created successfully ... user: $username / $password")
}