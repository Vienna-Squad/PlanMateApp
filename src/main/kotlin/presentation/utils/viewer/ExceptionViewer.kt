package org.example.presentation.utils.viewer

import org.example.data.exception.*
import org.example.data.exception.NotFoundException
import org.example.domain.exceptions.*
import org.example.presentation.exceptions.InvalidInputException

class ExceptionViewer : ItemViewer<Throwable> {
    override fun view(item: Throwable) {
        val message = when (item) {
            is FileAccessException -> "Access to the file was denied or corrupted file, Please check and try again"
            is CsvFormatException -> "corrupted files.please tray again "
            is WriteFailureException -> "Failed to write data. Please try again."
            is QueryFailureException -> "Failed to load data from the database. Please try again."
            is NetworkException -> "Network error while connecting. Check your internet connection."
            is AuthException -> "Authentication failed. Please check your database credentials."
            is ConfigException -> "Database configuration error. Please contact support."

            is ServerFailureException -> "A server-side error occurred . Please try again later."

            is ProjectNotFoundException -> "The specified project was not found."
            is NoProjectsFoundException -> "No projects found for your account."
            is TaskNotFoundException -> "The specified task was not found."
            is NoTasksFoundException -> "No tasks found."
            is UserNotFoundException -> "The specified user was not found."
            is NoUsersFoundException -> "No users found."
            is LogsNotFoundException -> "The requested logs were not found."
            is NoLogsFoundException -> "No logs available."

            is AuthenticationException -> "Authentication failed. Please check your credentials and try again."
            is UnauthorizedException -> "You are not authorized to perform this action."

            is ProjectAccessDeniedException -> "Access to this project is denied."
            is TaskAccessDeniedException -> "Access to this task is denied."
            is FeatureAccessDeniedException -> "Access to this feature is restricted."

            is ProjectAlreadyExistsException -> "A project with this name already exists."
            is MateAlreadyExistsException -> "This mate already exists in the project."
            is StateAlreadyExistsException -> "This state already exists in the project."

            is NoChangeException -> "No changes were made."
            is UnknownException -> "An unknown error occurred. Please try again."
            is MateNotAssignedToTaskException -> "This mate is not assigned to the selected task."
            is MateNotInProjectException -> "This mate is not a member of the project."
            is StateNotInProjectException -> "This state is not defined in the project."

            is NotFoundException -> "The requested item was not found."

            is InvalidInputException,is IllegalArgumentException -> "Invalid input. Please check your data and try again."

            else -> "An unexpected error occurred. Please try again."
        }

        println("\u001B[31m$message\u001B[0m")
    }

}