package org.example.domain.repository

import org.example.domain.entity.Project

interface ProjectsRepository {
    fun get(projectId: String): Result<Project>
    fun getAll(): Result<List<Project>>
    fun add(project: Project): Result<Unit>
    fun update(project: Project): Result<Unit>
    fun delete(projectId: String): Result<Unit>
}