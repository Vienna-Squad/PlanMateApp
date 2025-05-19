package org.example.di

import data.datasource.local.csv.LogsCsvStorage
import data.datasource.local.csv.ProjectsCsvStorage
import data.datasource.local.csv.TasksCsvStorage
import data.datasource.local.csv.UsersCsvStorage
import data.datasource.local.preferences.Preferences
import data.datasource.remote.mongo.ProjectsMongoStorage
import data.datasource.remote.mongo.TasksMongoStorage
import data.datasource.remote.mongo.UsersMongoStorage
import org.bson.Document
import org.example.DiNamedLabels.LOGS
import org.example.DiNamedLabels.LOG_CSV_PARSER
import org.example.DiNamedLabels.LOG_MONGO_PARSER
import org.example.DiNamedLabels.PROJECTS
import org.example.DiNamedLabels.PROJECT_CSV_PARSER
import org.example.DiNamedLabels.PROJECT_MONGO_PARSER
import org.example.DiNamedLabels.TASKS
import org.example.DiNamedLabels.TASK_CSV_PARSER
import org.example.DiNamedLabels.TASK_MONGO_PARSER
import org.example.DiNamedLabels.USERS
import org.example.DiNamedLabels.USER_CSV_PARSER
import org.example.DiNamedLabels.USER_MONGO_PARSER
import org.example.FilesPaths.LOGS_FILE_PATH
import org.example.FilesPaths.PROJECTS_FILE_PATH
import org.example.FilesPaths.TASKS_FILE_PATH
import org.example.FilesPaths.USERS_FILE_PATH
import org.example.MongoCollections.LOGS_COLLECTION
import org.example.MongoCollections.PROJECTS_COLLECTION
import org.example.MongoCollections.TASKS_COLLECTION
import org.example.MongoCollections.USERS_COLLECTION
import org.example.NamedDataSources.LOGS_LOCAL_DATA_SOURCE
import org.example.NamedDataSources.LOGS_REMOTE_DATA_SOURCE
import org.example.NamedDataSources.PROJECTS_LOCAL_DATA_SOURCE
import org.example.NamedDataSources.PROJECTS_REMOTE_DATA_SOURCE
import org.example.NamedDataSources.TASKS_LOCAL_DATA_SOURCE
import org.example.NamedDataSources.TASKS_REMOTE_DATA_SOURCE
import org.example.NamedDataSources.USERS_LOCAL_DATA_SOURCE
import org.example.NamedDataSources.USERS_REMOTE_DATA_SOURCE
import org.example.data.datasource.DataSource
import org.example.data.datasource.UnEditableDataSource
import org.example.data.datasource.local.csv.manager.LogsCsvFileManager
import org.example.data.datasource.local.csv.manager.ProjectsCsvFileManager
import org.example.data.datasource.local.csv.manager.TasksCsvFileManager
import org.example.data.datasource.local.csv.manager.UsersCsvFileManager
import org.example.data.datasource.local.csv.manager.base.CsvFileManager
import org.example.data.datasource.local.csv.manager.base.UnEditableCsvFileManager
import org.example.data.datasource.local.csv.parser.LogCsvParser
import org.example.data.datasource.local.csv.parser.ProjectCsvParser
import org.example.data.datasource.local.csv.parser.TaskCsvParser
import org.example.data.datasource.local.csv.parser.UserCsvParser
import org.example.data.datasource.remote.mongo.LogsMongoStorage
import org.example.data.datasource.remote.mongo.manager.LogsMongoManager
import org.example.data.datasource.remote.mongo.manager.ProjectsMongoManager
import org.example.data.datasource.remote.mongo.manager.TasksMongoManager
import org.example.data.datasource.remote.mongo.manager.UsersMongoManager
import org.example.data.datasource.remote.mongo.manager.base.MongoManager
import org.example.data.datasource.remote.mongo.manager.base.UnEditableMongoManager
import org.example.data.datasource.remote.mongo.parser.LogMongoParser
import org.example.data.datasource.remote.mongo.parser.ProjectMongoParser
import org.example.data.datasource.remote.mongo.parser.TaskMongoParser
import org.example.data.datasource.remote.mongo.parser.UserMongoParser
import org.example.data.utils.Parser
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.log.Log
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single { Preferences }

    //region CSV
    //Parsers
    single<Parser<String, Log>>(named(LOG_CSV_PARSER)) { LogCsvParser() }
    single<Parser<String, Project>>(named(PROJECT_CSV_PARSER)) { ProjectCsvParser() }
    single<Parser<String, Task>>(named(TASK_CSV_PARSER)) { TaskCsvParser() }
    single<Parser<String, User>>(named(USER_CSV_PARSER)) { UserCsvParser() }
    //Managers
    single<UnEditableCsvFileManager<Log>>(named(LOGS)) { LogsCsvFileManager(LOGS_FILE_PATH, get(named(LOG_CSV_PARSER))) }
    single<CsvFileManager<Project>>(named(PROJECTS)) {
        ProjectsCsvFileManager(
            PROJECTS_FILE_PATH,
            get(named(PROJECT_CSV_PARSER))
        )
    }
    single<CsvFileManager<Task>>(named(TASKS)) { TasksCsvFileManager(TASKS_FILE_PATH, get(named(TASK_CSV_PARSER))) }
    single<CsvFileManager<User>>(named(USERS)) { UsersCsvFileManager(USERS_FILE_PATH, get(named(USER_CSV_PARSER))) }
    //endregion

    //region Mongo
    //Parsers
    single<Parser<Document, Log>>(named(LOG_MONGO_PARSER)) { LogMongoParser() }
    single<Parser<Document, Project>>(named(PROJECT_MONGO_PARSER)) { ProjectMongoParser() }
    single<Parser<Document, Task>>(named(TASK_MONGO_PARSER)) { TaskMongoParser() }
    single<Parser<Document, User>>(named(USER_MONGO_PARSER)) { UserMongoParser() }
    //Managers
    single<UnEditableMongoManager<Log>>(named(LOGS)) { LogsMongoManager(LOGS_COLLECTION, get(named(LOG_MONGO_PARSER))) }
    single<MongoManager<Project>>(named(PROJECTS)) {
        ProjectsMongoManager(
            PROJECTS_COLLECTION,
            get(named(PROJECT_MONGO_PARSER))
        )
    }
    single<MongoManager<Task>>(named(TASKS)) { TasksMongoManager(TASKS_COLLECTION, get(named(TASK_MONGO_PARSER))) }
    single<MongoManager<User>>(named(USERS)) { UsersMongoManager(USERS_COLLECTION, get(named(USER_MONGO_PARSER))) }
    //endregion


    single<UnEditableDataSource<Log>>(named(LOGS_LOCAL_DATA_SOURCE)) { LogsCsvStorage(get(named(LOGS))) }
    single<DataSource<Project>>(named(PROJECTS_LOCAL_DATA_SOURCE)) { ProjectsCsvStorage(get(named(PROJECTS))) }
    single<DataSource<Task>>(named(TASKS_LOCAL_DATA_SOURCE)) { TasksCsvStorage(get(named(TASKS))) }
    single<DataSource<User>>(named(USERS_LOCAL_DATA_SOURCE)) { UsersCsvStorage(get(named(USERS))) }

    single<UnEditableDataSource<Log>>(named(LOGS_REMOTE_DATA_SOURCE)) { LogsMongoStorage(get(named(LOGS))) }
    single<DataSource<Project>>(named(PROJECTS_REMOTE_DATA_SOURCE)) { ProjectsMongoStorage(get(named(PROJECTS))) }
    single<DataSource<Task>>(named(TASKS_REMOTE_DATA_SOURCE)) { TasksMongoStorage(get(named(TASKS))) }
    single<DataSource<User>>(named(USERS_REMOTE_DATA_SOURCE)) { UsersMongoStorage(get(named(USERS))) }
}
