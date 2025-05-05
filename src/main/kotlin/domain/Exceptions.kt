package org.example.domain

abstract class PlanMateAppException(message: String) : Throwable(message)

class LoginException(message: String = "LoginException!!") : PlanMateAppException(message)
class RegisterException(message: String = "RegisterException!!") : PlanMateAppException(message)
class UnauthorizedException(message: String = "Unauthorized!!") : PlanMateAppException(message)
class AccessDeniedException(message: String = "Access denied!!") : PlanMateAppException(message)
class NotFoundException(type: String = "NotFound!!") : PlanMateAppException("No $type found.")
class InvalidInputException(message: String = "InvalidInput!!") : PlanMateAppException(message)
class AlreadyExistException(message: String = "Already exist!!") : PlanMateAppException(message)
class UnknownException(message: String = "UnknownException!!") : PlanMateAppException(message)