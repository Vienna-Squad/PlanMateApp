package org.example.domain.repository

import org.example.domain.PlanMateAppException
import org.example.domain.UnknownException

interface Repository {
    fun <T> safeCall(bloc: () -> T): T {
        return try {
            bloc()
        } catch (planMateException: PlanMateAppException) {
            throw planMateException
        } catch (_: Exception) {
            throw UnknownException()
        }
    }
}