package org.example.data.storage.repository

import org.example.data.storage.ProjectCsvStorage
import org.example.domain.entity.Project
import org.example.domain.repository.ProjectsRepository

class ProjectsCsvRepository(
    private val storage: ProjectCsvStorage
) : ProjectsRepository {
    override fun get(projectId: String): Result<Project> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Result<List<Project>> {
        TODO("Not yet implemented")
    }

    override fun add(project: Project): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun update(project: Project): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun delete(projectId: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}