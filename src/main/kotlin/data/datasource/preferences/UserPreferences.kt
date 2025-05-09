package data.datasource.preferences

import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ID
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_NAME
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ROLE
import org.example.domain.UnauthorizedException
import org.example.domain.entity.User.UserRole
import java.io.File
import java.util.*

class UserPreferences(private val file: File) : Preference {
    private val map: MutableMap<String, String> = mutableMapOf()

    init {
        read()
    }

    override fun saveUser(
        userId: UUID,
        username: String,
        role: UserRole
    ) {
        map[CURRENT_USER_ID] = userId.toString()
        map[CURRENT_USER_NAME] = username
        map[CURRENT_USER_ROLE] = role.toString()
        write(map.toList())
    }

    override fun getCurrentUserID(): UUID =
        map.getOrElse(CURRENT_USER_ID) { throw UnauthorizedException() }.let { UUID.fromString(it) }

    override fun getCurrentUserName(): String = map.getOrElse(CURRENT_USER_NAME) { throw UnauthorizedException() }

    override fun getCurrentUserRole(): UserRole =
        map.getOrElse(CURRENT_USER_ROLE) { throw UnauthorizedException() }.let { UserRole.valueOf(it) }


    override fun clear() {
        map.clear()
        write(map.toList())
    }

    private fun write(items: List<Pair<String, String>>) {
        if (!file.exists()) file.createNewFile()
        val str = StringBuilder()
        items.forEach {
            str.append("${it.first},${it.second}\n")
        }
        file.writeText(str.toString())
    }

    private fun read() {
        if (file.exists()) {
            file.readLines().forEach { line ->
                val fields = line.split(",")
                map[fields[0]] = fields[1]
            }
        }
    }
}
