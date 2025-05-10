package di

import org.example.domain.usecase.auth.CreateUserUseCase
import org.example.domain.usecase.auth.LoginUseCase
import org.example.domain.usecase.auth.LogoutUseCase
import org.example.domain.usecase.project.*
import org.example.domain.usecase.task.*
import org.koin.dsl.module


val useCasesModule = module {
    single { LogoutUseCase(get()) }
    single { LoginUseCase(get()) }
    single { CreateUserUseCase(get(), get()) }
    single { AddMateToProjectUseCase(get(), get(), get()) }
    single { AddStateToProjectUseCase(get(), get(), get()) }
    single { CreateProjectUseCase(get(), get(), get()) }
    single { DeleteMateFromProjectUseCase(get(), get(), get()) }
    single { DeleteProjectUseCase(get(), get(), get()) }
    single { DeleteStateFromProjectUseCase(get(), get(), get()) }
    single { EditProjectNameUseCase(get(), get(), get()) }
    single { GetAllTasksOfProjectUseCase(get(), get(), get()) }
    single { GetProjectHistoryUseCase(get()) }
    single { CreateTaskUseCase(get(), get(), get(), get()) }
    single { GetProjectHistoryUseCase(get()) }
    single { DeleteTaskUseCase(get(), get(), get(), get()) }
    single { GetTaskHistoryUseCase(get()) }
    single { GetTaskUseCase(get(), get(), get()) }
    single { AddMateToTaskUseCase(get(), get(), get(), get()) }
    single { DeleteMateFromTaskUseCase(get(), get(), get(), get()) }
    single { EditTaskStateUseCase(get(), get(), get(), get()) }
    single { EditTaskTitleUseCase(get(), get(), get(), get()) }
    single { GetAllProjectsUseCase(get(), get()) }
}