package org.example.domain.usecase

import org.example.domain.exceptions.*
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import java.util.*

object Validator {
    fun canCreateUser(currentUser: User) {
        ensureFeatureAccess(currentUser)
    }
    fun canAddMateToProject(project: Project, currentUser: User, mateId: UUID) {
        ensureProjectOwner(project, currentUser)
        ensureMateNotInProject(project, mateId)
    }
    fun canAddStateToProject(project: Project, currentUser: User, stateName: String) {
        ensureProjectOwner(project, currentUser)
        ensureStateNotInProject(project, stateName)
    }
    fun canCreateProject(currentUser: User) {
        ensureFeatureAccess(currentUser)
    }
    fun canDeleteMateFromProject(project: Project, currentUser: User, mateId: UUID) {
        ensureProjectOwner(project, currentUser)
        ensureMateInProject(project, mateId)
    }
    fun canDeleteProject(project: Project, currentUser: User) {
        ensureProjectOwner(project, currentUser)
    }
    fun canDeleteStateFromProject(project: Project, currentUser: User) {
        ensureProjectOwner(project, currentUser)
    }
    fun canEditProjectName(project: Project, currentUser: User, newName: String) {
        ensureProjectOwner(project, currentUser)
        if(project.name == newName.trim()) throw NoChangeException()
    }
    fun canGetAllTasksOfProject(project: Project, currentUser: User) {
        ensureProjectAccess(project, currentUser)
    }
    fun canGetProjectHistory(project: Project, currentUser: User) {
        ensureProjectAccess(project, currentUser)
    }
    fun canAddMateToTask(project: Project, task: Task, currentUser: User, mateId: UUID) {
        ensureTaskAccess(project, currentUser)
        ensureMateInProject(project, mateId)
        ensureMateNotInTask(task, mateId)
    }
    fun canCreateTask(project: Project, currentUser: User, stateName: String) {
        ensureProjectAccess(project, currentUser)
        ensureStateInProject(project, stateName)
    }
    fun canDeleteMateFromTask(project: Project, task: Task, currentUser: User, mateId: UUID) {
        ensureTaskAccess(project, currentUser)
        ensureMateInTask(task, mateId)
    }
    fun canDeleteTask(project: Project, currentUser: User) {
        ensureTaskAccess(project, currentUser)
    }
    fun canEditTaskState(project: Project, task: Task, currentUser: User, stateName: String) {
        ensureTaskAccess(project, currentUser)
        if(task.state.name == stateName) throw NoChangeException()
    }
    fun canEditTaskTitle(project: Project, task: Task, currentUser: User, newTitle: String) {
        ensureTaskAccess(project, currentUser)
        if(task.title == newTitle) throw NoChangeException()
    }
    fun canGetTaskHistory(project: Project, currentUser: User) {
        ensureTaskAccess(project, currentUser)
    }
    fun canGetTask(project: Project, currentUser: User) {
        ensureTaskAccess(project, currentUser)
    }


    private fun ensureProjectOwner(project: Project, currentUser: User) {
        if (project.createdBy != currentUser.id) throw ProjectAccessDeniedException()
    }
    private fun ensureProjectAccess(project: Project, currentUser: User) {
        if (project.createdBy != currentUser.id && currentUser.id !in project.matesIds) throw ProjectAccessDeniedException()
    }
    private fun ensureTaskAccess(project: Project, currentUser: User) {
        if (project.createdBy != currentUser.id && currentUser.id !in project.matesIds) throw TaskAccessDeniedException()
    }
    private fun ensureFeatureAccess(currentUser: User) {
        if (currentUser.role != User.UserRole.ADMIN) throw FeatureAccessDeniedException()
    }

    private fun ensureMateInProject(project: Project, mateId: UUID) {
        if (mateId !in project.matesIds) throw MateNotInProjectException()
    }
    private fun ensureMateNotInProject(project: Project, mateId: UUID) {
        if (mateId in project.matesIds) throw MateAlreadyExistsException()
    }

    private fun ensureMateInTask(task: Task, mateId: UUID) {
        if (mateId !in task.assignedTo) throw MateNotAssignedToTaskException()
    }
    private fun ensureMateNotInTask(task: Task, mateId: UUID) {
        if (mateId in task.assignedTo) throw MateAlreadyExistsException()
    }

    private fun ensureStateNotInProject(project: Project, stateName: String) {
        if (project.states.any { it.name == stateName }) throw StateAlreadyExistsException()
    }
    private fun ensureStateInProject(project: Project, stateName: String) {
        if (project.states.all { it.name != stateName }) throw StateNotInProjectException()
    }
}