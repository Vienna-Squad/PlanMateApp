package org.example.common.di

import org.example.data.repository.AuthRepositoryImpl
import org.example.data.repository.LogsRepositoryImpl
import org.example.data.repository.ProjectsRepositoryImpl
import org.example.data.repository.TasksRepositoryImpl
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.koin.dsl.module


val repositoryModule = module {
        single<LogsRepository> { LogsRepositoryImpl(get()) }
        single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
        single<TasksRepository> { TasksRepositoryImpl(get()) }
        single<AuthRepository> { AuthRepositoryImpl(get(),get()) }
    }