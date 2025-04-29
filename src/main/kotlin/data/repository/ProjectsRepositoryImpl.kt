package org.example.data.repository

import data.storage.Storage
import org.example.domain.entity.Project
import org.example.domain.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val projectStorage: Storage<Project>
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