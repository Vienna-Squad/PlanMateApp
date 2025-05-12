package org.example.di

import data.datasource.DataSource
import data.datasource.csv.LogsCsvStorage
import data.datasource.csv.ProjectsCsvStorage
import data.datasource.csv.TasksCsvStorage
import data.datasource.csv.UsersCsvStorage
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
import org.example.data.config.StorageConfig
import org.example.data.config.StorageType
import org.example.domain.entity.log.Log
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single<Preference> { UserPreferences(File(Constants.Files.PREFERENCES_FILE_NAME)) }

    single<DataSource<Log>>(named(LOGS_DATA_SOURCE)) {
        if (StorageConfig.currentStorageType == StorageType.REMOTE) {
            LogsMongoStorage()
        } else {
            LogsCsvStorage(File(Constants.Files.LOGS_FILE_NAME))
        }
    }

    single<DataSource<Project>>(named(PROJECTS_DATA_SOURCE)) {
        if (StorageConfig.currentStorageType == StorageType.REMOTE) {
            ProjectsMongoStorage()
        } else {
            ProjectsCsvStorage(File(Constants.Files.PROJECTS_FILE_NAME))
        }
    }

    single<DataSource<Task>>(named(TASKS_DATA_SOURCE)) {
        if (StorageConfig.currentStorageType == StorageType.REMOTE) {
            TasksMongoStorage()
        } else {
            TasksCsvStorage(File(Constants.Files.TASKS_FILE_NAME))
        }
    }

    single<DataSource<User>>(named(USERS_DATA_SOURCE)) {
        if (StorageConfig.currentStorageType == StorageType.REMOTE) {
            UsersMongoStorage()
        } else {
            UsersCsvStorage(File(Constants.Files.USERS_FILE_NAME))
        }
    }
}