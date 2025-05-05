package org.example.domain.repository

import org.example.domain.entity.Project
import java.util.*

interface ProjectsRepository {
    fun getProjectById(projectId: UUID): Project
    fun getAllProjects(): List<Project>
    fun addMateToProject(projectId: UUID, mateId: UUID)
    fun addStateToProject(projectId: UUID, state: String)
    fun addProject(name: String)
    fun editProjectName(projectId: UUID, name: String)
    fun deleteMateFromProject(projectId: UUID, mateId: UUID)
    fun deleteProjectById(projectId: UUID)
    fun deleteStateFromProject(projectId: UUID, state: String)
}