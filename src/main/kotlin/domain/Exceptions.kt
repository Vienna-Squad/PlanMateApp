package org.example.domain

abstract class PlanMateAppException(message: String) : Exception(message)
class NoProjectFoundException() : PlanMateAppException("")
class NoTaskFoundException(message: String) : PlanMateAppException(message)
open class AuthException(message: String) : PlanMateAppException(message)
class LoginException() : AuthException("")
class RegisterException() : AuthException("")
class NoMateFoundException(): PlanMateAppException("")
class NoStateFoundException(): PlanMateAppException("")
class UnauthorizedException(): PlanMateAppException("")
class InvalidProjectIdException(): PlanMateAppException("")
class FailedToDeleteMate():PlanMateAppException("")
