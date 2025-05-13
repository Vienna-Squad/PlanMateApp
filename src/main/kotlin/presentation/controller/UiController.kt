package org.example.presentation.controller

import org.example.domain.*
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemViewer
import kotlin.io.AccessDeniedException

interface UiController {
    fun execute()
    fun tryAndShowError(
        exceptionViewer: ItemViewer<Throwable> = ExceptionViewer(),
        bloc: () -> Unit,
    ) {
        try {
            bloc()
        } catch (e: Throwable) {
            when (e) {
                is UnauthorizedException -> exceptionViewer.view("You are not logged in. Please sign in.")
                is AccessDeniedException -> exceptionViewer.view("You don’t have permission to perform this action.")
                is NotFoundException -> exceptionViewer.view("The requested item was not found.")
                is InvalidInputException -> exceptionViewer.view("Please check your input and try again.")
                is IllegalArgumentException->exceptionViewer.view("Please check your input and try again.")
                is AlreadyExistException -> exceptionViewer.view("This item already exists.")
                is UnknownException -> exceptionViewer.view("An unexpected error occurred.")
                is NoChangeException -> exceptionViewer.view("No changes were made.")
                is TaskHasNoException -> exceptionViewer.view("The selected task has no content.")
                is ProjectHasNoException -> exceptionViewer.view("The selected project is empty.")

                is FileReadException -> exceptionViewer.view("Could not read the file. Please check the format or path.")

                is WriteFailureException -> exceptionViewer.view("Unable to save your data. Please try again.")
                is QueryFailureException -> exceptionViewer.view("Something went wrong while retrieving data.")
                is NetworkException -> exceptionViewer.view("Network issue. Please check your internet connection.")
                is AuthException -> exceptionViewer.view("Database authentication failed. Please try again later.")
                is ConfigException -> exceptionViewer.view("Internal setup issue. Please contact support.")
                is ServerFailureException -> exceptionViewer.view("Server error. Please try again later.")

                else -> exceptionViewer.view("An unexpected error occurred. Please try again.")
            }
        }
    }
}