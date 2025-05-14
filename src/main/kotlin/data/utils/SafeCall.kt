package org.example.data.utils

import com.mongodb.*
import org.example.domain.exceptions.NetworkException
import org.example.domain.exceptions.PlanMateAppException
import org.example.domain.exceptions.StorageException
import org.example.domain.exceptions.UnknownException
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.MalformedInputException

fun <T> safeCall(bloc: () -> T): T {
    return try {
        bloc()
    } catch (e: Exception) {
        throw when (e) {
            is PlanMateAppException->e

            is MongoWriteException,
            is MongoWriteConcernException ,
            is MongoQueryException ,
            is MongoSocketReadException,
            is MongoSocketOpenException,
            is MongoTimeoutException ,
            is MongoConfigurationException,
            is MongoSecurityException ,
            is MongoServerException ->  NetworkException()

            is MalformedInputException,
            is CharacterCodingException,
            is IllegalArgumentException,
            is SecurityException,
            is FileNotFoundException,
            is IOException -> StorageException()

            else -> UnknownException()
        }
    }
}