package org.example.domain

abstract class PlanMateAppException : Throwable()

class AuthenticationException : PlanMateAppException()
class UnauthorizedException : PlanMateAppException()
class NotFoundException:PlanMateAppException()
class ProjectAccessDenied:PlanMateAppException()
class TaskAccessDenied:PlanMateAppException()


class ProjectNotFound : PlanMateAppException()
class NoProjectsFound : PlanMateAppException()

class TaskNotFound : PlanMateAppException()
class NoTasksFound : PlanMateAppException()

class UserNotFound:PlanMateAppException()
class NoUsersFound:PlanMateAppException()

class MateNotFound:PlanMateAppException()
class NoLogsFound:PlanMateAppException()
class LogsNotFound:PlanMateAppException()
class InvalidInputException : PlanMateAppException()

class MateAlreadyExists:PlanMateAppException()
class ProjectAlreadyExists:PlanMateAppException()
class TaskAlreadyExists:PlanMateAppException()
class StateAlreadyExists:PlanMateAppException()
class UserAlreadyExists:PlanMateAppException()
class UnknownException: PlanMateAppException()
class NoChangeException: PlanMateAppException()

class MateNotAssignedToTaskException: PlanMateAppException()

class TaskNotInProjectException : PlanMateAppException()
class MateNotInProjectException : PlanMateAppException()
class StateNotInProjectException : PlanMateAppException()

//csv
class FileAccessException : PlanMateAppException()
//mongo
class MongoWriteFailureException:PlanMateAppException()
class MongoQueryFailureException:PlanMateAppException()
class MongoNetworkException : PlanMateAppException()
class MongoAuthException : PlanMateAppException()
class MongoConfigException : PlanMateAppException()
class MongoServerFailureException : PlanMateAppException()