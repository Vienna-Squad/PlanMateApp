package org.example.di

import data.storage.UserCsvStorage
import org.example.data.storage.LogsCsvStorage
import org.example.data.storage.ProjectCsvStorage
import org.example.data.storage.TaskCsvStorage
import org.koin.dsl.module
import java.io.File

val dataModule = module {

    single { LogsCsvStorage(File("logs.csv")) }
    single { ProjectCsvStorage(File("projects.csv")) }
    single { TaskCsvStorage(File("tasks.csv")) }
    single { UserCsvStorage(File("users.csv")) }
}
