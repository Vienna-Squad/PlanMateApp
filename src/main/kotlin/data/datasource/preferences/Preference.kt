package data.datasource.preferences

import org.example.domain.entity.User.UserRole
import java.util.UUID

interface Preference {
    fun saveUser(userId: UUID, username: String, role: UserRole)
    fun getCurrentUserID(): UUID
    fun getCurrentUserName(): String
    fun getCurrentUserRole(): UserRole
    fun clear()
}