package org.example.domain

abstract class PlanMateAppException : Throwable()


class UnauthorizedException : PlanMateAppException()
class AccessDeniedException : PlanMateAppException()
class NotFoundException : PlanMateAppException()
class InvalidInputException : PlanMateAppException()
class AlreadyExistException : PlanMateAppException()
class UnknownException : PlanMateAppException()
class NoChangeException : PlanMateAppException()
class TaskHasNoException : PlanMateAppException()
class ProjectHasNoException : PlanMateAppException()

//csv

//mongo
class WriteFailureException:PlanMateAppException()
class QueryFailureException:PlanMateAppException()
class NetworkException : PlanMateAppException()
class AuthException : PlanMateAppException()
class ConfigException : PlanMateAppException()
class ServerFailureException : PlanMateAppException()