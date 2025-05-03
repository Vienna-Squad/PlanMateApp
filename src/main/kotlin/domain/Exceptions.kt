package org.example.domain

abstract class PlanMateAppException(message: String) : Exception(message)

class LoginException(message: String) : PlanMateAppException(message)
class RegisterException(message: String) : PlanMateAppException(message)
class UnauthorizedException(message: String) : PlanMateAppException(message)
class AccessDeniedException(message: String) : PlanMateAppException(message)
class NotFoundException(message: String) : PlanMateAppException(message)
class InvalidIdException(message: String) : PlanMateAppException(message)
class AlreadyExistException(message: String) : PlanMateAppException(message)
class FailedToAddLogException(message: String):PlanMateAppException(message)
class UnknownException(message: String) : PlanMateAppException(message)
class FailedToLogException(message: String): PlanMateAppException(message)
class FailedToAddException(message: String): PlanMateAppException(message)
class FailedToCreateProject(message: String):PlanMateAppException(message)
class FailedToCallLogException(message: String):PlanMateAppException(message)