package org.example.data.utils

import org.example.domain.PlanMateAppException
import org.example.domain.UnknownException


fun <T> safeCall(bloc: () -> T): T {
    return try {
        bloc()
    } catch (planMateException: PlanMateAppException) {
        throw planMateException
    } catch (_: Exception) {
        throw UnknownException()
    }
}
