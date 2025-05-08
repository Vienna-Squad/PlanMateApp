package org.example.data.utils

import data.datasource.DataSource
import org.example.common.Constants
import org.example.common.Constants.NamedDataSources.USERS_DATA_SOURCE
import data.datasource.preferences.Preference
import org.example.domain.NotFoundException
import org.example.domain.PlanMateAppException
import org.example.domain.UnauthorizedException
import org.example.domain.UnknownException
import org.example.domain.entity.User
import java.util.*


class SafeExecutor(
    private val usersRemoteDataSource: DataSource<User>,
    private val preferences: Preference,
) {
    fun <T> authCall(bloc: (user: User) -> T): T {
        return try {
            preferences.get(Constants.PreferenceKeys.CURRENT_USER_ID)?.let { userId ->
                usersRemoteDataSource.getAll().find { it.id == UUID.fromString(userId) }?.let { user ->
                    bloc(user)
                } ?: throw NotFoundException("username or password")
            } ?: throw UnauthorizedException()
        } catch (planMateException: PlanMateAppException) {
            throw planMateException
        } catch (_: Exception) {
            throw UnknownException()
        }
    }

    fun <T> call(bloc: () -> T): T {
        return try {
            bloc()
        } catch (planMateException: PlanMateAppException) {
            throw planMateException
        } catch (_: Exception) {
            throw UnknownException()
        }
    }
}
