package org.example.domain

abstract class PlanMateAppException(message: String) : Exception(message)

class LoginException() : PlanMateAppException("")
class RegisterException() : PlanMateAppException("")
class UnauthorizedException() : PlanMateAppException("")
class AccessDeniedException() : PlanMateAppException("")
class NoFoundException() : PlanMateAppException("")
class InvalidIdException() : PlanMateAppException("")
class AlreadyExistException() : PlanMateAppException("")
class FailedToAddLogException():PlanMateAppException("")
class UnknownException() : PlanMateAppException("")
class FailedToLogException(): PlanMateAppException("")
class FailedToAddException(): PlanMateAppException("")
class FailedToCreateProject():PlanMateAppException("")

class FailedToCallLogException():PlanMateAppException("")