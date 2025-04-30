package org.example.domain

abstract class PlanMateAppException(message: String) : Exception(message)

open class AuthException(message: String) : PlanMateAppException(message)
class LoginException() : AuthException("")
class RegisterException() : AuthException("")
class UnauthorizedException() : AuthException("")
class AccessDeniedException() : PlanMateAppException("")
class NoFoundException() : PlanMateAppException("")
class InvalidIdException() : PlanMateAppException("")
class AlreadyExistException() : PlanMateAppException("")
class UnknownException() : PlanMateAppException("")