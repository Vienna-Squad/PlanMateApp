package org.example.data.storage.repository

import org.example.data.storage.ProjectCsvStorage
import org.example.domain.NotFoundException
import org.example.domain.entity.Project
import org.example.domain.repository.ProjectsRepository
import java.util.UUID

class ProjectsRepositoryImpl(
    private val storage: ProjectCsvStorage
) : ProjectsRepository {

    override fun getProjectById(projectId: UUID): Result<Project> {
        return runCatching {
            storage.read().find { it.id == projectId }
                ?: throw NotFoundException( "Project not found")
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun getAllProjects(): Result<List<Project>> {
        return runCatching {
            storage.read()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun addProject(project: Project): Result<Unit> {
        return runCatching {
            storage.append(project)
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }

    override fun updateProject(project: Project): Result<Unit> {
        return runCatching {
            val projects = storage.read().toMutableList()
            val index = projects.indexOfFirst { it.id == project.id }
            if (index != -1) {
                projects[index] = project
                storage.write(projects)
            } else {
                throw NotFoundException( "Project not found")
            }
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }

    override fun deleteProjectById(projectId: UUID): Result<Unit> {
        return runCatching {
            val projects = storage.read().toMutableList()
            val removed = projects.removeIf { it.id == projectId }
            if (removed) {
                storage.write(projects)
            } else {
                throw NotFoundException( "Project not found")
            }
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }
}
