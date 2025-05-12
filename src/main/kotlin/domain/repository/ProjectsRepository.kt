package org.example.domain.repository

import org.example.domain.entity.Project
import java.util.*

interface ProjectsRepository : Repository {
    fun getProjectById(projectId: UUID): Project
    fun getAllProjects(): List<Project>
    fun addProject(project: Project)
    fun updateProject(updatedProject: Project)
    fun deleteProjectById(projectId: UUID)
}