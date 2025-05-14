package org.example.domain.exceptions


abstract class PlanMateAppException : Throwable()

class AuthenticationException : PlanMateAppException()

class UnauthorizedException : PlanMateAppException()

class NotFoundException : PlanMateAppException()


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
