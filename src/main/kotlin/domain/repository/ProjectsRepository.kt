package org.example.domain.repository

import org.example.domain.entity.Project

interface ProjectsRepository {
    fun getProjects(): Result<List<Project>>
    fun addProject(project: Project)
}