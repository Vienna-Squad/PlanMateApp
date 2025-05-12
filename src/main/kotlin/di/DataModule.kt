package org.example.di

import data.datasource.csv.LogsCsvStorage
import data.datasource.csv.ProjectsCsvStorage
import data.datasource.csv.TasksCsvStorage
import data.datasource.csv.UsersCsvStorage
import data.datasource.mongo.LogsMongoStorage
import data.datasource.mongo.ProjectsMongoStorage
import data.datasource.mongo.TasksMongoStorage
import data.datasource.mongo.UsersMongoStorage
import org.example.common.FilesPaths.LOGS_FILE_PATH
import org.example.common.NamedDataSources.LOGS_LOCAL_DATA_SOURCE
import org.example.common.NamedDataSources.LOGS_REMOTE_DATA_SOURCE
import org.example.common.NamedDataSources.PROJECTS_LOCAL_DATA_SOURCE
import org.example.common.NamedDataSources.PROJECTS_REMOTE_DATA_SOURCE
import org.example.common.NamedDataSources.TASKS_LOCAL_DATA_SOURCE
import org.example.common.NamedDataSources.TASKS_REMOTE_DATA_SOURCE
import org.example.common.NamedDataSources.USERS_LOCAL_DATA_SOURCE
import org.example.common.NamedDataSources.USERS_REMOTE_DATA_SOURCE
import org.example.common.bases.DataSource
import org.example.common.bases.UnEditableCsvFileManager
import org.example.common.bases.UnEditableDataSource
import org.example.data.datasource.csv.parser.LogCsvParser
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.log.Log
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single<UnEditableCsvFileManager<Log>> { UnEditableCsvFileManager(LOGS_FILE_PATH, LogCsvParser()) }

    single<UnEditableDataSource<Log>>(named(LOGS_LOCAL_DATA_SOURCE)) { LogsCsvStorage(get()) }
    single<DataSource<Project>>(named(PROJECTS_LOCAL_DATA_SOURCE)) { ProjectsCsvStorage() }
    single<DataSource<Task>>(named(TASKS_LOCAL_DATA_SOURCE)) { TasksCsvStorage() }
    single<DataSource<User>>(named(USERS_LOCAL_DATA_SOURCE)) { UsersCsvStorage() }

    single<DataSource<Log>>(named(LOGS_REMOTE_DATA_SOURCE)) { LogsMongoStorage() }
    single<DataSource<Project>>(named(PROJECTS_REMOTE_DATA_SOURCE)) { ProjectsMongoStorage() }
    single<DataSource<Task>>(named(TASKS_REMOTE_DATA_SOURCE)) { TasksMongoStorage() }
    single<DataSource<User>>(named(USERS_REMOTE_DATA_SOURCE)) { UsersMongoStorage() }
}
