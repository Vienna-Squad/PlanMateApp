package org.example.data.exception


abstract class DataExceptions:Exception ()

class FileAccessException : DataExceptions()

class CsvFormatException  : DataExceptions()

class WriteFailureException : DataExceptions()

class QueryFailureException : DataExceptions()

class NetworkException : DataExceptions()
class AuthException : DataExceptions()
class ConfigException : DataExceptions()
class ServerFailureException : DataExceptions()


abstract class NotFoundException:DataExceptions()

class ProjectNotFoundException : NotFoundException()
class NoProjectsFoundException : NotFoundException()

class TaskNotFoundException : NotFoundException()
class NoTasksFoundException : NotFoundException()

class UserNotFoundException : NotFoundException()
class NoUsersFoundException : NotFoundException()

class NoLogsFoundException : NotFoundException()
class LogsNotFoundException : NotFoundException()
