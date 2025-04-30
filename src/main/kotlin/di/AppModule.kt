package di

import org.example.presentation.app.AuthApp
import org.koin.dsl.module


val authAppModule = module {
    single { AuthApp() }
}