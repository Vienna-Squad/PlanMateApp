package data.datasource.preferences

import org.example.common.PreferenceKeys.CURRENT_USER_ID
import org.example.domain.UnauthorizedException
import java.util.*

class CurrentUserPreferences {
    private var map: MutableMap<String, String> = mutableMapOf()

    fun saveCurrentUserId(userId: UUID) {
        map[CURRENT_USER_ID] = userId.toString()
    }

    fun getCurrentUserId() = map[CURRENT_USER_ID]?.let { UUID.fromString(it) } ?: throw UnauthorizedException()

    fun clear() = map.clear()
}