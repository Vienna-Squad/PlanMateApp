package org.example.common

object Constants {
    object APPS {
        const val AUTH_APP = "AUTH_APP"
        const val ADMIN_APP = "ADMIN_APP"
        const val MATE_APP = "MATE_APP"
    }

    object Files {
        const val PREFERENCES_FILE_NAME = "preferences.csv"
        const val LOGS_FILE_NAME = "logs.csv"
        const val PROJECTS_FILE_NAME = "projects.csv"
        const val TASKS_FILE_NAME = "tasks.csv"
        const val USERS_FILE_NAME = "users.csv"
    }

    object PreferenceKeys {
        const val CURRENT_USER_ID = "CURRENT_USER_ID"
        const val CURRENT_USER_NAME = "CURRENT_USER_NAME"
        const val CURRENT_USER_ROLE = "CURRENT_USER_ROLE"
    }

    object MongoCollections {
        const val LOGS_COLLECTION = "LOGS_COLLECTION"
        const val TASKS_COLLECTION = "TASKS_COLLECTION"
        const val PROJECTS_COLLECTION = "PROJECTS_COLLECTION"
        const val USERS_COLLECTION = "USERS_COLLECTION"
    }

    object NamedDataSources {
        const val LOGS_DATA_SOURCE = "LOGS_DATA_SOURCE"
        const val TASKS_DATA_SOURCE = "TASKS_DATA_SOURCE"
        const val PROJECTS_DATA_SOURCE = "PROJECTS_DATA_SOURCE"
        const val USERS_DATA_SOURCE = "USERS_DATA_SOURCE"
    }
}