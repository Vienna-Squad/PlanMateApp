package org.example.di

import data.datasource.local.csv.LogsCsvStorage
import data.datasource.local.csv.ProjectsCsvStorage
import data.datasource.local.csv.TasksCsvStorage
import data.datasource.local.csv.UsersCsvStorage
import data.datasource.local.preferences.Preferences
import data.datasource.remote.mongo.LogsMongoStorage
import data.datasource.remote.mongo.ProjectsMongoStorage
import data.datasource.remote.mongo.TasksMongoStorage
import data.datasource.remote.mongo.UsersMongoStorage
import org.example.DiNamedLabels.LOGS
import org.example.DiNamedLabels.PROJECTS
import org.example.DiNamedLabels.TASKS
import org.example.DiNamedLabels.USERS
import org.example.FilesPaths.LOGS_FILE_PATH
import org.example.FilesPaths.PROJECTS_FILE_PATH
import org.example.FilesPaths.TASKS_FILE_PATH
import org.example.FilesPaths.USERS_FILE_PATH
import org.example.NamedDataSources.LOGS_LOCAL_DATA_SOURCE
import org.example.NamedDataSources.LOGS_REMOTE_DATA_SOURCE
import org.example.NamedDataSources.PROJECTS_LOCAL_DATA_SOURCE
import org.example.NamedDataSources.PROJECTS_REMOTE_DATA_SOURCE
import org.example.NamedDataSources.TASKS_LOCAL_DATA_SOURCE
import org.example.NamedDataSources.TASKS_REMOTE_DATA_SOURCE
import org.example.NamedDataSources.USERS_LOCAL_DATA_SOURCE
import org.example.NamedDataSources.USERS_REMOTE_DATA_SOURCE
import org.example.data.datasource.local.bases.CsvFileManager
import org.example.data.datasource.DataSource
import org.example.data.datasource.local.bases.Parser
import org.example.data.datasource.local.bases.UnEditableCsvFileManager
import org.example.data.datasource.UnEditableDataSource
import org.example.data.datasource.local.csv.manager.LogsCsvFileManager
import org.example.data.datasource.local.csv.manager.ProjectsCsvFileManager
import org.example.data.datasource.local.csv.manager.TasksCsvFileManager
import org.example.data.datasource.local.csv.manager.UsersCsvFileManager
import org.example.data.datasource.local.csv.parser.LogParser
import org.example.data.datasource.local.csv.parser.ProjectParser
import org.example.data.datasource.local.csv.parser.TaskParser
import org.example.data.datasource.local.csv.parser.UserParser
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.log.Log
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single { Preferences }

    //Parsers
    single<Parser<Log>>(named(LOGS)) { LogParser() }
    single<Parser<Project>>(named(PROJECTS)) { ProjectParser() }
    single<Parser<Task>>(named(TASKS)) { TaskParser() }
    single<Parser<User>>(named(USERS)) { UserParser() }

    //CsvFileManagers
    single<UnEditableCsvFileManager<Log>>(named(LOGS)) { LogsCsvFileManager(LOGS_FILE_PATH, get(named(LOGS))) }
    single<CsvFileManager<Project>>(named(PROJECTS)) {
        ProjectsCsvFileManager(
            PROJECTS_FILE_PATH,
            get(named(PROJECTS))
        )
    }
    single<CsvFileManager<Task>>(named(TASKS)) { TasksCsvFileManager(TASKS_FILE_PATH, get(named(TASKS))) }
    single<CsvFileManager<User>>(named(USERS)) { UsersCsvFileManager(USERS_FILE_PATH, get(named(USERS))) }




    single<UnEditableDataSource<Log>>(named(LOGS_LOCAL_DATA_SOURCE)) { LogsCsvStorage(get(named(LOGS))) }
    single<DataSource<Project>>(named(PROJECTS_LOCAL_DATA_SOURCE)) { ProjectsCsvStorage(get(named(PROJECTS))) }
    single<DataSource<Task>>(named(TASKS_LOCAL_DATA_SOURCE)) { TasksCsvStorage(get(named(TASKS))) }
    single<DataSource<User>>(named(USERS_LOCAL_DATA_SOURCE)) { UsersCsvStorage(get(named(USERS))) }

    single<DataSource<Log>>(named(LOGS_REMOTE_DATA_SOURCE)) { LogsMongoStorage() }
    single<DataSource<Project>>(named(PROJECTS_REMOTE_DATA_SOURCE)) { ProjectsMongoStorage() }
    single<DataSource<Task>>(named(TASKS_REMOTE_DATA_SOURCE)) { TasksMongoStorage() }
    single<DataSource<User>>(named(USERS_REMOTE_DATA_SOURCE)) { UsersMongoStorage() }
}
