package org.example.domain.repository

import org.example.domain.entity.Project
import java.util.*

interface ProjectsRepository {
    fun getProjectById(projectId: UUID): Result<Project>
    fun getAllProjects(): Result<List<Project>>
    fun addMateToProject(projectId: UUID, mateId: UUID): Result<Unit>
    fun addStateToProject(projectId: UUID, state: String): Result<Unit>
    fun addProject(name: String): Result<Unit>
    fun editProjectName(projectId: UUID,name: String): Result<Unit>
    fun deleteMateFromProject(projectId: UUID, mateId: UUID): Result<Unit>
    fun deleteProjectById(projectId: UUID): Result<Unit>
    fun deleteStateFromProject(projectId: UUID, state: String): Result<Unit>
}