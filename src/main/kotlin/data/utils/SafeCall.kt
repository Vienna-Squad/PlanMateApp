package org.example.data.utils

import org.example.common.Constants
import org.example.data.datasource.local.csv.CsvStorage
import org.example.data.datasource.local.preferences.CsvPreferences
import org.example.domain.PlanMateAppException
import org.example.domain.UnauthorizedException
import org.example.domain.UnknownException
import org.example.domain.entity.User
import org.koin.mp.KoinPlatform
import java.util.*

fun <T> authSafeCall(
    usersCsvStorage: CsvStorage<User> = KoinPlatform.getKoin().get(),
    preferences: CsvPreferences = KoinPlatform.getKoin().get(),
    bloc: (user: User) -> T
): T {
    return try {
        preferences.get(Constants.PreferenceKeys.CURRENT_USER_ID)?.let { userId ->
            usersCsvStorage.getAll().find { it.id == UUID.fromString(userId) }?.let { user ->
                bloc(user)
            } ?: throw UnauthorizedException()
        } ?: throw UnauthorizedException()
    } catch (planMateException: PlanMateAppException) {
        throw planMateException
    } catch (_: Exception) {
        throw UnknownException()
    }
}

fun <T> safeCall(bloc: () -> T): T {
    return try {
        bloc()
    } catch (planMateException: PlanMateAppException) {
        throw planMateException
    } catch (_: Exception) {
        throw UnknownException()
    }
}
