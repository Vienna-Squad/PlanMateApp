package di

import org.koin.dsl.module

import data.storage.UserCsvStorage
import org.example.data.storage.LogsCsvStorage
import org.example.data.storage.ProjectCsvStorage
import org.example.data.storage.TaskCsvStorage
import org.example.data.storage.repository.AuthenticationCsvRepository
import org.example.domain.entity.Log
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.repository.*
import org.example.presentation.AuthApp

import org.koin.dsl.module
import java.io.File

val appModule = module {
    // Storage directory configuration
    single {
        val dataDir = "data"
        File(dataDir).apply {
            if (!exists()) mkdirs()
        }
        dataDir
    }

    // CSV Storage implementations
    single {
        UserCsvStorage(File(get<String>(), "users.csv"))
    }

    single {
        ProjectCsvStorage(File(get<String>(), "projects.csv"))
    }

    single {
        TaskCsvStorage(File(get<String>(), "tasks.csv"))
    }

    single {
        LogsCsvStorage(File(get<String>(), "logs.csv"))
    }

    // Repository implementations
    single<AuthenticationRepository> {
        AuthenticationCsvRepository(get(), null)
    }

    single<ProjectsRepository> {
        // Create your ProjectsRepository implementation
        object : ProjectsRepository {
            override fun get(projectId: String): Result<Project> =
                Result.failure(Exception("Not implemented yet"))

            override fun getAll(): Result<List<Project>> =
                Result.success(emptyList())

            override fun add(project: Project): Result<Unit> =
                Result.success(Unit)

            override fun update(project: Project): Result<Unit> =
                Result.success(Unit)

            override fun delete(projectId: String): Result<Unit> =
                Result.success(Unit)
        }
    }

    single<TasksRepository> {
        // Create your TasksRepository implementation
        object : TasksRepository {
            override fun get(taskId: String): Result<Task> =
                Result.failure(Exception("Not implemented yet"))

            override fun getAll(): Result<List<Task>> =
                Result.success(emptyList())

            override fun add(task: Task): Result<Unit> =
                Result.success(Unit)

            override fun update(task: Task): Result<Unit> =
                Result.success(Unit)

            override fun delete(taskId: String): Result<Unit> =
                Result.success(Unit)
        }
    }

    single<LogsRepository> {
        // Create your LogsRepository implementation
        object : LogsRepository {
            override fun getAll(): Result<List<Log>> =
                Result.success(emptyList())

            override fun add(log: Log): Result<Unit> =
                Result.success(Unit)
        }
    }
    // UI components
    single { AuthApp() }
   // single {  }

}