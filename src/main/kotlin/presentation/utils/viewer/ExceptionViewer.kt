package org.example.presentation.utils.viewer

import org.example.domain.*

class ExceptionViewer : ItemViewer<Throwable> {
    override fun view(item: Throwable) {
        val message = when (item) {
            // Auth & Access Exceptions
            is AuthenticationException -> "Authentication failed. Please check your credentials and try again."
            is ProjectAccessDenied -> "Access to this project is denied."
            is TaskAccessDenied -> "Access to this task is denied."

            // Not Found Exceptions
            is ProjectNotFound -> "The specified project was not found."
            is TaskNotFound -> "The specified task was not found."
            is MateNotFound -> "The specified mate was not found."

            // Input Errors
            is InvalidInputException -> "Invalid input. Please check the fields and try again."
            is IllegalArgumentException -> "Invalid argument provided."

            // Already Exists
            is MateAlreadyExists -> "This mate is already part of the system."
            is ProjectAlreadyExists -> "A project with this name already exists."
            is TaskAlreadyExists -> "A task with this name already exists."
            is StateAlreadyExists -> "This state already exists in the project."
            is UserAlreadyExists -> "A user with this information already exists."

            is UnknownException -> "An unknown error occurred. Please try again."
            is NoChangeException -> "No changes detected. Nothing was updated."

            is MateNotAssignedToTaskException -> "This mate is not assigned to the selected task."
            is TaskNotInProjectException -> "This task does not belong to the current project."
            is MateNotInProjectException -> "This mate is not part of the current project."
            is StateNotInProjectException -> "This state is not available in the project."

            // CSV / File Errors
            is FileAccessException -> "File access error. Unable to read or process the file."
            // MongoDB Errors
            is MongoWriteFailureException -> "Failed to write data to the database."
            is MongoQueryFailureException -> "Failed to retrieve data from the database."
            is MongoNetworkException -> "A network error occurred while accessing the database."
            is MongoAuthException -> "Database authentication failed. Please verify credentials."
            is MongoConfigException -> "Database configuration error. Please contact support."
            is MongoServerFailureException -> "A server error occurred on the database side."

            // Fallback
            else -> "An unexpected error occurred. Please try again."
        }

        println("\u001B[31m${message}\u001B[0m")
    }
}