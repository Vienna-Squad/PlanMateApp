package org.example.data.utils

import com.mongodb.*
import org.example.domain.*
import java.io.FileNotFoundException
import java.io.IOException

fun <T> safeCall(bloc: () -> T): T {
    return try {
        bloc()
    } catch (planMateException: PlanMateAppException) {
        throw planMateException
    } catch (e: Exception) {
        throw when (e) {
            is MongoWriteException -> MongoWriteFailureException()
            is MongoWriteConcernException -> MongoWriteFailureException()
            is MongoQueryException -> MongoQueryFailureException()
            is MongoSocketReadException -> MongoNetworkException()
            is MongoSocketOpenException -> MongoNetworkException()
            is MongoTimeoutException -> MongoNetworkException()
            is MongoSecurityException -> MongoAuthException()
            is MongoConfigurationException -> MongoConfigException()
            is MongoServerException -> MongoServerFailureException()

            is FileNotFoundException -> FileAccessException()
            is IOException -> FileAccessException()
            is SecurityException -> FileAccessException()
            else -> UnknownExceptionException()
        }


    }
}