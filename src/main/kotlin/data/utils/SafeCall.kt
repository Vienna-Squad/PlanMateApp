package org.example.data.utils

import com.mongodb.*
import org.example.domain.exceptions.NetworkException
import org.example.domain.exceptions.StorageException
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.MalformedInputException

fun <T> safeCall(bloc: () -> T): T {
    return try {
        bloc()
    } catch (exception: Exception) {
        throw when (exception) {
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

            else -> exception
        }
    }
}