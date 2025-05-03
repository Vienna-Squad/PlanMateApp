package org.example

import di.appModule
import di.useCasesModule
import org.example.di.dataModule
import org.example.di.repositoryModule
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.presentation.AuthApp
import org.koin.core.context.GlobalContext.startKoin
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

fun main() {
    println("Hello, PlanMate!")
    startKoin { modules(appModule, useCasesModule, repositoryModule, dataModule) }
    AuthApp().run()
}

