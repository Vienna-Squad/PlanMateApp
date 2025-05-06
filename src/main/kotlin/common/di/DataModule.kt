package org.example.common.di

import data.datasource.local.csv.UsersCsvStorage
import org.example.common.Constants
import org.example.common.Constants.NamedDataSources.LOGS_DATA_SOURCE
import org.example.common.Constants.NamedDataSources.PROJECTS_DATA_SOURCE
import org.example.common.Constants.NamedDataSources.TASKS_DATA_SOURCE
import org.example.common.Constants.NamedDataSources.USERS_DATA_SOURCE
import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.csv.LogsCsvStorage
import org.example.data.datasource.local.csv.ProjectsCsvStorage
import org.example.data.datasource.local.csv.TasksCsvStorage
import org.example.data.datasource.local.preferences.CsvPreferences
import org.example.data.datasource.local.preferences.Preference
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.datasource.remote.mongo.LogsMongoStorage
import org.example.data.datasource.remote.mongo.ProjectsMongoStorage
import org.example.data.datasource.remote.mongo.TasksMongoStorage
import org.example.data.datasource.remote.mongo.UsersMongoStorage
import org.example.domain.entity.Log
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single<Preference> { CsvPreferences(File(Constants.Files.PREFERENCES_FILE_NAME)) }

    single<RemoteDataSource<Log>>(named(LOGS_DATA_SOURCE)) { LogsMongoStorage() }
    single<RemoteDataSource<Project>>(named(PROJECTS_DATA_SOURCE)) { ProjectsMongoStorage() }
    single<RemoteDataSource<Task>>(named(TASKS_DATA_SOURCE)) { TasksMongoStorage() }
    single<RemoteDataSource<User>>(named(USERS_DATA_SOURCE)) { UsersMongoStorage() }

    single<LocalDataSource<Log>>(named(LOGS_DATA_SOURCE)) { LogsCsvStorage(File(Constants.Files.LOGS_FILE_NAME)) }
    single<LocalDataSource<Project>>(named(PROJECTS_DATA_SOURCE)) { ProjectsCsvStorage(File(Constants.Files.PROJECTS_FILE_NAME)) }
    single<LocalDataSource<Task>>(named(TASKS_DATA_SOURCE)) { TasksCsvStorage(File(Constants.Files.TASKS_FILE_NAME)) }
    single<LocalDataSource<User>>(named(USERS_DATA_SOURCE)) { UsersCsvStorage(File(Constants.Files.USERS_FILE_NAME)) }
}
