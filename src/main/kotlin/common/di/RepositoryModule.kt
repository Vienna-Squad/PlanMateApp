package org.example.common.di

import org.example.common.Constants.NamedDataSources.LOGS_DATA_SOURCE
import org.example.common.Constants.NamedDataSources.PROJECTS_DATA_SOURCE
import org.example.common.Constants.NamedDataSources.TASKS_DATA_SOURCE
import org.example.common.Constants.NamedDataSources.USERS_DATA_SOURCE
import org.example.data.repository.LogsRepositoryImpl
import org.example.data.repository.ProjectsRepositoryImpl
import org.example.data.repository.TasksRepositoryImpl
import org.example.data.repository.UsersRepositoryImpl
import org.example.data.utils.SafeExecutor
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module


val repositoryModule = module {
    single { SafeExecutor(get(), get(named(USERS_DATA_SOURCE))) }


    single<LogsRepository> { LogsRepositoryImpl(get(named(LOGS_DATA_SOURCE)),get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get(named(PROJECTS_DATA_SOURCE)),get()) }
    single<TasksRepository> { TasksRepositoryImpl(get(named(TASKS_DATA_SOURCE)),get()) }
    single<UsersRepository> { UsersRepositoryImpl(get(named(USERS_DATA_SOURCE)), get(),get()) }
}