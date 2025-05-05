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
}