package data.datasource.preferences

import org.example.common.PreferenceKeys.CURRENT_USER_ID
import org.example.common.PreferenceKeys.DATA_SOURCE_TYPE
import org.example.common.StorageType
import org.example.domain.exceptions.UnauthorizedException
import java.util.*

object Preferences {
    private var map: MutableMap<String, String> = mutableMapOf()

    fun saveCurrentUserId(userId: UUID) {
        map[CURRENT_USER_ID] = userId.toString()
    }

    fun saveDataSourceType(type: StorageType) {
        map[DATA_SOURCE_TYPE] = type.toString()
    }

    fun getCurrentUserId() = map[CURRENT_USER_ID]?.let { UUID.fromString(it) } ?: throw UnauthorizedException()

    fun getDataSourceType() = map[DATA_SOURCE_TYPE]?.let { StorageType.valueOf(it.uppercase()) } ?: StorageType.LOCAL

    fun clear() = map.clear()
}