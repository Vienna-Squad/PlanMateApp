package org.example.presentation.utils.viewer

import org.example.domain.*

class ExceptionViewer : ItemViewer<Throwable> {
    override fun view(item: Throwable) {
        val message = when (item) {
            // Auth & Access Exceptions
            is AuthenticationException -> "Authentication failed. Please check your credentials and try again."
            is UnauthorizedException -> "You are not authorized. To Do This Action"
            is ProjectAccessDeniedException -> "Access to this project is denied."

            // Not Found Exceptions
            is ProjectNotFoundException -> "The specified project was not found."
            is NoProjectsFoundException -> "No projects found for your account."
            is TaskNotFoundException -> "The specified task was not found."
            is NoTasksFoundException -> "No tasks found."
            is UserNotFoundException -> "The specified user was not found."
            is NoUsersFoundException -> "No users found."
            is LogsNotFoundException -> "The requested logs were not found."
            is NoLogsFoundException -> "No logs available."

            // Input Errors
            is InvalidInputException -> "Invalid input. Please check the fields and try again."
            is IllegalArgumentException -> "Invalid argument provided."

            // Already Exists Exceptions
            is MateAlreadyExistsException -> "This mate already exists in the project."
            is StateAlreadyExistsException -> "This state already exists in the project."

            // Domain Logic Exceptions
            is UnknownException -> "An unknown error occurred. Please try again."
            is NoChangeException -> "No changes were made."
            is MateNotAssignedToTaskException -> "This mate is not assigned to the selected task."
            is ProjectHasNoThisMateException -> "This mate is not a member of the project."
            is ProjectHasNoThisStateException -> "This state is not defined in the project."

            // CSV / File Exceptions
            is FileAccessException -> "Failed to read or access the CSV file. Please check the path or format."

            // MongoDB Exceptions
            is MongoWriteFailureException -> "Failed to write data to the database. Please try again."
            is MongoQueryFailureException -> "Failed to retrieve data from the database. Please try again."
            is MongoNetworkException -> "Network error while connecting to the database. Check your internet connection."
            is MongoAuthException -> "Authentication failed. Please check your database credentials."
            is MongoConfigException -> "Database configuration error. Please contact support."
            is MongoServerFailureException -> "A server-side error occurred in the database. Please try again later."

            // Fallback
            else -> "An unexpected error occurred. Please try again."
        }

        println("\u001B[31m$message\u001B[0m")
    }

}