package org.example

import common.di.appModule
import common.di.useCasesModule
import org.example.common.di.dataModule
import org.example.common.di.repositoryModule
import org.example.presentation.AuthApp
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    println("Hello, PlanMate!")
    startKoin { modules(appModule, useCasesModule, repositoryModule, dataModule) }
    AuthApp().run()
}

