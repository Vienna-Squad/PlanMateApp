package org.example.domain

abstract class PlanMateAppException(message: String) : Exception(message)

open class AuthException(message: String) : PlanMateAppException(message)
class LoginException(message: String = "") : AuthException(message)
class RegisterException(message: String = "") : AuthException(message)
class UnauthorizedException(message: String = "") : AuthException(message)
class AccessDeniedException(message: String = "") : PlanMateAppException(message)
class NoFoundException(message: String = "") : PlanMateAppException(message)
class InvalidIdException(message: String = "") : PlanMateAppException(message)
class AlreadyExistException(message: String = "") : PlanMateAppException(message)
class UnknownException(message: String = "") : PlanMateAppException(message)