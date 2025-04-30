package org.example

import di.authAppModule
import di.useCasesModule
import org.example.presentation.app.AuthApp
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    println("Hello, PlanMate!")
    startKoin { modules(authAppModule, useCasesModule) }
    AuthApp().run()
}