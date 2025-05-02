package org.example.domain.repository

import org.example.domain.entity.Project
import java.util.UUID

interface ProjectsRepository {
    fun getProjectById(projectId: UUID): Result<Project>
    fun getAllProjects(): Result<List<Project>>
    fun addProject(project: Project): Result<Unit>
    fun updateProject(project: Project): Result<Unit>
    fun deleteProjectById(projectId: UUID): Result<Unit>
}