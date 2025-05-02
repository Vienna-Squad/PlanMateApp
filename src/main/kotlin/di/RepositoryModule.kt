package org.example.di

import org.example.data.storage.repository.AuthenticationRepositoryImpl
import org.example.data.storage.repository.LogsRepositoryImpl
import org.example.data.storage.repository.ProjectsRepositoryImpl
import org.example.data.storage.repository.TasksRepositoryImpl
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.koin.dsl.module


val repositoryModule =
    module {
        single { LogsRepositoryImpl(get()) }
        single { ProjectsRepositoryImpl(get()) }
        single { TasksRepositoryImpl(get()) }
        single { AuthenticationRepositoryImpl(get()) }
    }