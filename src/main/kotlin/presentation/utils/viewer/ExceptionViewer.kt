package org.example.presentation.utils.viewer


import org.example.domain.exceptions.*

class ExceptionViewer : ItemViewer<Throwable> {
    override fun view(item: Throwable) {
        val message = when (item) {
            is AuthenticationException -> "You must be logged in to perform this action."
            is UnauthorizedException -> "You do not have permission to access this resource."
            is ProjectAccessDeniedException -> "You are not allowed to access this project."
            is TaskAccessDeniedException -> "You are not allowed to access this task."
            is FeatureAccessDeniedException -> "This feature is not available for your account."
            is ProjectAlreadyExistsException -> "A project with the same name already exists."
            is MateAlreadyExistsException -> "This mate is already part of the project."
            is StateAlreadyExistsException -> "This state already exists in the project."
            is NoChangeException -> "No changes were detected."
            is UnknownException -> "An unknown error occurred. Please try again."
            is MateNotAssignedToTaskException -> "This mate is not assigned to the task."
            is MateNotInProjectException -> "This mate is not a member of the project."
            is StateNotInProjectException -> "The specified state is not part of the project."
            is ProjectNotFoundException -> "The specified project could not be found."
            is NoProjectsFoundException -> "You have no projects available."
            is TaskNotFoundException -> "The specified task could not be found."
            is NoTasksFoundException -> "No tasks found."
            is UserNotFoundException -> "User not found."
            is NoUsersFoundException -> "No users available."
            is NoLogsFoundException -> "No logs found for this request."
            is StorageException -> "A storage error occurred. Please try again later."
            is NetworkException -> "Network error. Please check your connection and try again."
            is AdditionException -> "Failed to add the item. Please try again."
            is DeletionException -> "Failed to delete the item. Please try again."
            is ModificationException -> "Failed to modify the item. Please try again."
            is InvalidInputException -> "The input provided is invalid. Please check and try again."
            else -> "An unexpected error occurred. Please contact support."
        }

        println("\u001B[31m$message\u001B[0m")
    }

}