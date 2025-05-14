package org.example.domain.exceptions


abstract class PlanMateAppException : Exception()

class AuthenticationException : PlanMateAppException()

class UnauthorizedException : PlanMateAppException()

class ProjectAccessDeniedException : PlanMateAppException()
class TaskAccessDeniedException : PlanMateAppException()
class FeatureAccessDeniedException : PlanMateAppException()

class ProjectAlreadyExistsException : PlanMateAppException()
class MateAlreadyExistsException : PlanMateAppException()
class StateAlreadyExistsException : PlanMateAppException()
class NoChangeException : PlanMateAppException()

class UnknownException : PlanMateAppException()

class MateNotAssignedToTaskException : PlanMateAppException()

class MateNotInProjectException : PlanMateAppException()
class StateNotInProjectException : PlanMateAppException()

open class NotFoundException : PlanMateAppException()

class ProjectNotFoundException : NotFoundException()
class NoProjectsFoundException : NotFoundException()

class TaskNotFoundException : NotFoundException()
class NoTasksFoundException : NotFoundException()

class UserNotFoundException : NotFoundException()
class NoUsersFoundException : NotFoundException()

class NoLogsFoundException : NotFoundException()
class LogsNotFoundException : NotFoundException()

abstract class DataExceptions:PlanMateAppException ()
class StorageException : DataExceptions()
class NetworkException : DataExceptions()
