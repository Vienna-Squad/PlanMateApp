package domain.repository

import org.example.domain.entity.Project

interface StatesRepository {
    fun addState(projectId: String , state:String)
    fun deleteStateFromProject(projectId: String, state: String) : Boolean
}