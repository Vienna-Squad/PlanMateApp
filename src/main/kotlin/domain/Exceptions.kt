package org.example.domain

abstract class PlanMateAppException : Throwable()

class AuthenticationException : PlanMateAppException()
class UnauthorizedException : PlanMateAppException()
class NotFoundException:PlanMateAppException()
class ProjectAccessDenied:PlanMateAppException()
class TaskAccessDenied:PlanMateAppException()


class ProjectNotFoundException : PlanMateAppException()
class NoProjectsFoundException : PlanMateAppException()

class TaskNotFoundException : PlanMateAppException()
class NoTasksFoundException : PlanMateAppException()

class UserNotFoundException:PlanMateAppException()
class NoUsersFoundException:PlanMateAppException()

class MateNotFoundException:PlanMateAppException()
class NoLogsFoundException:PlanMateAppException()
class LogsNotFoundException:PlanMateAppException()
class InvalidInputException : PlanMateAppException()

class MateAlreadyExistsException:PlanMateAppException()
class StateAlreadyExistsException:PlanMateAppException()

class UnknownExceptionException: PlanMateAppException()
class NoChangeExceptionException: PlanMateAppException()

class MateNotAssignedToTaskException: PlanMateAppException()

class ProjectHasNoThisMate : PlanMateAppException()
class ProjectHasNoThisState : PlanMateAppException()

//csv
class FileAccessException : PlanMateAppException()
//mongo
class MongoWriteFailureException:PlanMateAppException()
class MongoQueryFailureException:PlanMateAppException()
class MongoNetworkException : PlanMateAppException()
class MongoAuthException : PlanMateAppException()
class MongoConfigException : PlanMateAppException()
class MongoServerFailureException : PlanMateAppException()