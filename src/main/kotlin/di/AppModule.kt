package di

import org.koin.dsl.module

import data.storage.UserCsvStorage
import org.example.data.storage.LogsCsvStorage
import org.example.data.storage.ProjectCsvStorage
import org.example.data.storage.TaskCsvStorage
import org.example.data.storage.repository.AuthenticationRepositoryImpl
import org.example.data.storage.repository.LogsRepositoryImpl
import org.example.data.storage.repository.ProjectsRepositoryImpl
import org.example.data.storage.repository.TasksRepositoryImpl
import org.example.domain.entity.Log
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.repository.*
import org.example.presentation.AdminApp
import org.example.presentation.App
import org.example.presentation.AuthApp
import org.example.presentation.MateApp
import org.example.presentation.controller.ExitUiController
import org.example.presentation.controller.LoginUiController
import org.example.presentation.controller.RegisterUiController
import org.koin.core.qualifier.named

import java.io.File

val appModule = module {
    // Storage directory configuration
    single {
        val dataDir = "data"
        File(dataDir).apply {
            if (!exists()) mkdirs()
        }
        dataDir
    }
    single { UserCsvStorage(File(get<String>(), "users.csv")) }

    single { ProjectCsvStorage(File(get<String>(), "projects.csv")) }

    single { TaskCsvStorage(File(get<String>(), "tasks.csv")) }

    single { LogsCsvStorage(File(get<String>(), "logs.csv")) }

    // Repository implementations
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), null) }
    single<LogsRepository> { LogsRepositoryImpl(get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get(),) }
    single<TasksRepository> { TasksRepositoryImpl(get()) }

    // UI components
    single<App>(named("admin")) { AdminApp() }
    single<App>(named("auth")) { AuthApp() }
    single<App>(named("mate")) { MateApp() }
    single { LoginUiController() }
    single { RegisterUiController() }
    single { ExitUiController() }
    single { LoginUiController() }

    single { LogsRepositoryImpl(get()) }
    single { TasksRepositoryImpl(get()) }
    single { ProjectsRepositoryImpl(get()) }
    single { AuthenticationRepositoryImpl(get()) }

}