package di

import org.example.domain.usecase.auth.LoginUseCase
import org.example.domain.usecase.auth.LogoutUseCase
import org.example.domain.usecase.auth.RegisterUserUseCase
import org.example.domain.usecase.project.*
import org.example.domain.usecase.task.*
import org.koin.dsl.module


val useCasesModule = module {
    single { LoginUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { RegisterUserUseCase(get()) }
    single { AddMateToProjectUseCase(get(),get(),get()) }
    single { AddStateToProjectUseCase(get()) }
    single { CreateProjectUseCase(get(),get(),get()) }
    single { DeleteMateFromProjectUseCase(get(),get(),get()) }
    single { DeleteProjectUseCase(get(), get(), get()) }
    single { DeleteStateFromProjectUseCase(get()) }
    single { EditProjectNameUseCase(get(),get(),get()) }
    single { EditProjectStatesUseCase(get(),get(),get()) }
    single { GetAllTasksOfProjectUseCase(get(),get(),get()) }
    single { GetProjectHistoryUseCase(get(),get(),get()) }
    single { CreateTaskUseCase(get(), get (),get(),get()) }
    single { GetProjectHistoryUseCase(get(),get(),get()) }
    single { DeleteTaskUseCase(get()) }
    single { GetTaskHistoryUseCase(get()) }
    single { GetTaskUseCase(get(),get()) }
    single { AddMateToTaskUseCase(get(),get(),get(),get()) }
    single { DeleteMateFromTaskUseCase(get(),get(),get()) }
    single { EditTaskStateUseCase(get()) }
    single { EditTaskTitleUseCase(get(),get(),get()) }
}