package org.example

import di.appModule
import di.useCasesModule
import org.example.presentation.AuthApp
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    println("Hello, PlanMate!")
    startKoin { modules(appModule, useCasesModule) }
    AuthApp().run()
}