package org.example.data.storage.repository

import org.example.data.storage.ProjectCsvStorage
import org.example.domain.NoFoundException
import org.example.domain.entity.Project
import org.example.domain.repository.ProjectsRepository

class ProjectsCsvRepository(
    private val storage: ProjectCsvStorage
) : ProjectsRepository {

    override fun get(projectId: String): Result<Project> {
        return runCatching {
            storage.read().find { it.id == projectId }
                ?: throw NoFoundException()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun getAll(): Result<List<Project>> {
        return runCatching {
            storage.read()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun add(project: Project): Result<Unit> {
        return runCatching {
            storage.append(project)
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }

    override fun update(project: Project): Result<Unit> {
        return runCatching {
            val projects = storage.read().toMutableList()
            val index = projects.indexOfFirst { it.id == project.id }
            if (index != -1) {
                projects[index] = project
                storage.write(projects)
            } else {
                throw NoFoundException()
            }
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }

    override fun delete(projectId: String): Result<Unit> {
        return runCatching {
            val projects = storage.read().toMutableList()
            val removed = projects.removeIf { it.id == projectId }
            if (removed) {
                storage.write(projects)
            } else {
                throw NoFoundException()
            }
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }
}
