package org.example.common.di

import data.datasource.local.csv.UsersCsvStorage
import org.example.common.Constants
import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.csv.LogsCsvStorage
import org.example.data.datasource.local.csv.ProjectsCsvStorage
import org.example.data.datasource.local.csv.TasksCsvStorage
import org.example.data.datasource.local.preferences.CsvPreferences
import org.example.data.datasource.local.preferences.Preference
import org.example.domain.entity.Log
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single<Preference> { CsvPreferences(File(Constants.Files.PREFERENCES_FILE_NAME)) }
    single<LocalDataSource<Log>> { LogsCsvStorage(File(Constants.Files.LOGS_FILE_NAME)) }
    single<LocalDataSource<Project>> { ProjectsCsvStorage(File(Constants.Files.PROJECTS_FILE_NAME)) }
    single<LocalDataSource<Task>> { TasksCsvStorage(File(Constants.Files.TASKS_FILE_NAME)) }
    single<LocalDataSource<User>> { UsersCsvStorage(File(Constants.Files.USERS_FILE_NAME)) }
}
