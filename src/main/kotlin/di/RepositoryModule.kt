package org.example.di

import org.example.common.NamedDataSources.LOGS_LOCAL_DATA_SOURCE
import org.example.common.NamedDataSources.LOGS_REMOTE_DATA_SOURCE
import org.example.common.NamedDataSources.PROJECTS_LOCAL_DATA_SOURCE
import org.example.common.NamedDataSources.PROJECTS_REMOTE_DATA_SOURCE
import org.example.common.NamedDataSources.TASKS_LOCAL_DATA_SOURCE
import org.example.common.NamedDataSources.TASKS_REMOTE_DATA_SOURCE
import org.example.common.NamedDataSources.USERS_LOCAL_DATA_SOURCE
import org.example.common.NamedDataSources.USERS_REMOTE_DATA_SOURCE
import org.example.data.repository.LogsRepositoryImpl
import org.example.data.repository.ProjectsRepositoryImpl
import org.example.data.repository.TasksRepositoryImpl
import org.example.data.repository.UsersRepositoryImpl
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<LogsRepository> { LogsRepositoryImpl(get(named(LOGS_LOCAL_DATA_SOURCE)),get(named(LOGS_REMOTE_DATA_SOURCE))) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get(named(PROJECTS_LOCAL_DATA_SOURCE)),get(named(PROJECTS_REMOTE_DATA_SOURCE))) }
    single<TasksRepository> { TasksRepositoryImpl(get(named(TASKS_LOCAL_DATA_SOURCE)),get(named(TASKS_REMOTE_DATA_SOURCE))) }
    single<UsersRepository> { UsersRepositoryImpl(get(named(USERS_LOCAL_DATA_SOURCE)),get(named(USERS_REMOTE_DATA_SOURCE))) }
}