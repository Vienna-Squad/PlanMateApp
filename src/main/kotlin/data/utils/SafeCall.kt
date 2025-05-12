package org.example.data.utils

import com.mongodb.*
import org.example.domain.*

fun <T> safeCall(bloc: () -> T): T {
    return try {
        bloc()
    } catch (planMateException: PlanMateAppException) {
        throw planMateException
    } catch (e: Exception) {
        throw when (e) {



            is MongoWriteException -> WriteFailureException()
            is MongoWriteConcernException -> WriteFailureException()
            is MongoQueryException -> QueryFailureException()
            is MongoSocketReadException -> NetworkException()
            is MongoSocketOpenException -> NetworkException()
            is MongoTimeoutException -> NetworkException()
            is MongoSecurityException -> AuthException()
            is MongoConfigurationException -> ConfigException()
            is MongoServerException -> ServerFailureException()
            else -> UnknownException()
        }


    }
}