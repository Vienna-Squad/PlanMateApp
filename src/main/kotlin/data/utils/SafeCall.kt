package org.example.data.utils

import org.example.common.Constants
import org.example.data.datasource.local.preferences.Preference
import org.example.data.datasource.remote.RemoteDataSource
import org.example.domain.NotFoundException
import org.example.domain.PlanMateAppException
import org.example.domain.UnauthorizedException
import org.example.domain.UnknownException
import org.example.domain.entity.User
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatform.getKoin
import java.util.*


fun <T> authSafeCall(
    usersRemoteDataSource: RemoteDataSource<User> = getKoin().get(named("user")),
    preferences: Preference = getKoin().get(),
    bloc: (user: User) -> T
): T {
    return try {
        preferences.get(Constants.PreferenceKeys.CURRENT_USER_ID)?.let { userId ->
            usersRemoteDataSource.getAll().find { it.id == UUID.fromString(userId) }?.let { user ->
                bloc(user)
            } ?: throw NotFoundException("username or password")
        } ?: throw UnauthorizedException()
    } catch (planMateException: PlanMateAppException) {
        throw planMateException
    } catch (e: Exception) {
        e.printStackTrace()
        throw UnknownException()
    }
}

fun <T> safeCall(bloc: () -> T): T {
    return try {
        bloc()
    } catch (planMateException: PlanMateAppException) {
        throw planMateException
    } catch (e: Exception) {
        e.printStackTrace()
        throw UnknownException()
    }
}
