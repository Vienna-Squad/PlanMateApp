package org.example.di

import data.datasource.DataSource
import data.datasource.mongo.LogsMongoStorage
import data.datasource.mongo.ProjectsMongoStorage
import data.datasource.mongo.TasksMongoStorage
import data.datasource.mongo.UsersMongoStorage
import data.datasource.preferences.UserPreferences
import data.datasource.preferences.Preference
import org.example.common.Constants
import org.example.common.Constants.NamedDataSources.LOGS_DATA_SOURCE
import org.example.common.Constants.NamedDataSources.PROJECTS_DATA_SOURCE
import org.example.common.Constants.NamedDataSources.TASKS_DATA_SOURCE
import org.example.common.Constants.NamedDataSources.USERS_DATA_SOURCE
import org.example.domain.entity.log.Log
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single<Preference> { UserPreferences(File(Constants.Files.PREFERENCES_FILE_NAME)) }

    single<DataSource<Log>>(named(LOGS_DATA_SOURCE)) { LogsMongoStorage() }
    single<DataSource<Project>>(named(PROJECTS_DATA_SOURCE)) { ProjectsMongoStorage() }
    single<DataSource<Task>>(named(TASKS_DATA_SOURCE)) { TasksMongoStorage() }
    single<DataSource<User>>(named(USERS_DATA_SOURCE)) { UsersMongoStorage() }
}
