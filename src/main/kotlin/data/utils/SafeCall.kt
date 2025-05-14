package org.example.data.utils

import com.mongodb.*
import org.example.data.exception.*
import org.example.domain.exceptions.UnknownException
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.MalformedInputException

fun <T> safeCall(bloc: () -> T): T {
    return try {
        bloc()
    } catch (e: Exception) {
        throw when (e) {

            is NotFoundException->e

            is MongoWriteException,
            is MongoWriteConcernException -> WriteFailureException()
            is MongoQueryException -> QueryFailureException()

            is MongoSocketReadException,
            is MongoSocketOpenException,
            is MongoTimeoutException -> NetworkException()

            is MongoSecurityException -> AuthException()

            is MongoConfigurationException -> ConfigException()

            is MongoServerException -> ServerFailureException()

            is MalformedInputException,
            is CharacterCodingException,
            is ArrayIndexOutOfBoundsException,
            is IndexOutOfBoundsException,
            is IllegalArgumentException,
            is NullPointerException -> CsvFormatException()

            is SecurityException,
            is FileNotFoundException,
            is IOException -> FileAccessException()

            else -> UnknownException()
        }
    }
}