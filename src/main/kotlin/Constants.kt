package org.example

object APPS {
    const val AUTH_APP = "AUTH_APP"
    const val ADMIN_APP = "ADMIN_APP"
    const val MATE_APP = "MATE_APP"
}

object PreferenceKeys {
    const val CURRENT_USER_ID = "CURRENT_USER_ID"
    const val DATA_SOURCE_TYPE = "DATA_SOURCE_TYPE"
}

object FilesPaths {
    const val LOGS_FILE_PATH = "logs.csv"
    const val PROJECTS_FILE_PATH = "projects.csv"
    const val TASKS_FILE_PATH = "tasks.csv"
    const val USERS_FILE_PATH = "users.csv"
}

object MongoCollections {
    const val LOGS_COLLECTION = "LOGS_COLLECTION"
    const val TASKS_COLLECTION = "TASKS_COLLECTION"
    const val PROJECTS_COLLECTION = "PROJECTS_COLLECTION"
    const val USERS_COLLECTION = "USERS_COLLECTION"
}

object NamedDataSources {
    const val LOGS_LOCAL_DATA_SOURCE = "LOGS_LOCAL_DATA_SOURCE"
    const val TASKS_LOCAL_DATA_SOURCE = "TASKS_LOCAL_DATA_SOURCE"
    const val PROJECTS_LOCAL_DATA_SOURCE = "PROJECTS_LOCAL_DATA_SOURCE"
    const val USERS_LOCAL_DATA_SOURCE = "USERS_LOCAL_DATA_SOURCE"

    const val LOGS_REMOTE_DATA_SOURCE = "LOGS_REMOTE_DATA_SOURCE"
    const val TASKS_REMOTE_DATA_SOURCE = "TASKS_REMOTE_DATA_SOURCE"
    const val PROJECTS_REMOTE_DATA_SOURCE = "PROJECTS_REMOTE_DATA_SOURCE"
    const val USERS_REMOTE_DATA_SOURCE = "USERS_REMOTE_DATA_SOURCE"
}

object DiNamedLabels {
    const val LOGS = "LOGS"
    const val TASKS = "TASKS"
    const val PROJECTS = "PROJECTS"
    const val USERS = "USERS"

    const val LOG_CSV_PARSER = "LOG_CSV_PARSER"
    const val TASK_CSV_PARSER = "TASK_CSV_PARSER"
    const val PROJECT_CSV_PARSER = "PROJECT_CSV_PARSER"
    const val USER_CSV_PARSER = "USER_CSV_PARSER"

    const val LOG_MONGO_PARSER = "LOG_MONGO_PARSER"
    const val TASK_MONGO_PARSER = "TASK_MONGO_PARSER"
    const val PROJECT_MONGO_PARSER = "PROJECT_MONGO_PARSER"
    const val USER_MONGO_PARSER = "USER_MONGO_PARSER"
}

enum class StorageType {
    LOCAL,
    REMOTE
}