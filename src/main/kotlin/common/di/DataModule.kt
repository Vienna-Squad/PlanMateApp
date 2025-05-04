package org.example.common.di

import data.datasource.csv.UsersCsvStorage
import org.example.common.Constants
import org.example.data.datasource.csv.LogsCsvStorage
import org.example.data.datasource.csv.ProjectsCsvStorage
import org.example.data.datasource.csv.TasksCsvStorage
import org.example.data.datasource.preferences.CsvPreferences
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single { CsvPreferences(File(Constants.Files.PREFERENCES_FILE_NAME)) }
    single { LogsCsvStorage(File(Constants.Files.LOGS_FILE_NAME)) }
    single { ProjectsCsvStorage(File(Constants.Files.PROJECTS_FILE_NAME)) }
    single { TasksCsvStorage(File(Constants.Files.TASKS_FILE_NAME)) }
    single { UsersCsvStorage(File(Constants.Files.USERS_FILE_NAME)) }
}
