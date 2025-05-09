package org.example.domain

abstract class PlanMateAppException(message: String) : Throwable(message)

class LoginException(message: String = "LoginException!!") : PlanMateAppException(message)
class RegisterException(message: String = "RegisterException!!") : PlanMateAppException(message)
class UnauthorizedException() : PlanMateAppException("You are not authorized.")
class AccessDeniedException(type: String) : PlanMateAppException("You do not have access for this $type.")
class NotFoundException(type: String = "") : PlanMateAppException("Not $type found.")
class InvalidInputException(message: String? = null) : PlanMateAppException("${message ?: "Invalid input provided."} ")
class AlreadyExistException(type: String) : PlanMateAppException("The $type already exist.")
class UnknownException() : PlanMateAppException("Something went wrong.")
class NoChangeException() : PlanMateAppException("There is no any change.")
class TaskHasNoException(type: String) : PlanMateAppException("Task has no this $type.")
class ProjectHasNoException(type: String) : PlanMateAppException("Project has no this $type.")