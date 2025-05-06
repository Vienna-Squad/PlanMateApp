package org.example.domain.repository

import org.example.domain.entity.Project
import java.util.*

interface ProjectsRepository {
    fun getProjectById(projectId: UUID): Project
    fun getAllProjects(): List<Project>
    fun addProject(name: String)
    fun updateProject(updatedProject: Project)
    fun deleteProjectById(projectId: UUID)
}