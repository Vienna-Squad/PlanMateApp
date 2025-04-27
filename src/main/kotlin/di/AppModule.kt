package di

import org.example.presentation.AuthApp
import org.koin.dsl.module


val authAppModule = module {
    single { AuthApp() }
}