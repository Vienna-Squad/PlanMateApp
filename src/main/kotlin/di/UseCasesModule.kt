package di

import org.example.domain.usecase.auth.LoginUseCase
import org.example.domain.usecase.auth.RegisterUserUseCase
import org.example.domain.usecase.project.*
import org.example.domain.usecase.task.*
import org.koin.dsl.module


val useCasesModule = module {
    single { LoginUseCase(get()) }
    single { RegisterUserUseCase(get()) }
    single { AddMateToProjectUseCase(get()) }
    single { AddStateToProjectUseCase(get()) }
    single { CreateProjectUseCase(get(),get(),get()) }
    single { DeleteMateFromProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { DeleteStateFromProjectUseCase(get()) }
    single { EditProjectNameUseCase(get()) }
    single { GetAllTasksOfProjectUseCase(get()) }
    single { GetProjectHistoryUseCase(get()) }
    single { CreateTaskUseCase(get(), get (),get(),get()) }
    single { DeleteTaskUseCase(get()) }
    single { GetTaskHistoryUseCase(get()) }
    single { GetTaskUseCase(get()) }
    single { AddMateToTaskUseCase(get()) }
    single { DeleteMateFromTaskUseCase(get()) }
    single { EditTaskStateUseCase(get()) }
    single { EditTaskTitleUseCase(get()) }
}