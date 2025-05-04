package org.example.domain

abstract class PlanMateAppException(message: String) : Throwable(message)

class LoginException(message: String) : PlanMateAppException(message)
class RegisterException(message: String) : PlanMateAppException(message)
class UnauthorizedException(message: String = "Unauthorized!!") : PlanMateAppException(message)
class AccessDeniedException(message: String = "Access denied!!") : PlanMateAppException(message)
class NotFoundException(type: String) : PlanMateAppException("No $type found!!")
class InvalidIdException(message: String) : PlanMateAppException(message)
class AlreadyExistException(message: String = "Already exist!!") : PlanMateAppException(message)
class FailedToAddLogException(message: String) : PlanMateAppException(message)
class UnknownException(message: String) : PlanMateAppException(message)
class FailedToLogException(message: String) : PlanMateAppException(message)
class FailedToAddException(message: String) : PlanMateAppException(message)
class FailedToCreateProject(message: String) : PlanMateAppException(message)
class FailedToCallLogException(message: String) : PlanMateAppException(message)