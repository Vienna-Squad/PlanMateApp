package org.example.di

import org.example.data.storage.repository.AuthenticationCsvRepository
import org.example.data.storage.repository.LogsCsvRepository
import org.example.data.storage.repository.ProjectsCsvRepository
import org.example.data.storage.repository.TasksCsvRepository
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.koin.dsl.module


val repositoryModule =
    module{
    single<LogsRepository> { LogsCsvRepository(get()) }
single<ProjectsRepository> { ProjectsCsvRepository(get()) }
single<TasksRepository> { TasksCsvRepository(get()) }
single<AuthenticationRepository> { AuthenticationCsvRepository(get()) }}