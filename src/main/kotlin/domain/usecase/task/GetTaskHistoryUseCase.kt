package org.example.domain.usecase.task

import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.Log
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.koin.java.KoinJavaComponent.getKoin

class GetTaskHistoryUseCase(
    private val authenticationRepository: AuthenticationRepository=getKoin().get(),
    private val logsRepository: LogsRepository=getKoin().get())
{
    operator fun invoke(taskId: String): List<Log> {
         authenticationRepository.getCurrentUser().getOrElse {
          throw UnauthorizedException()
        }
        return logsRepository.getAll()
            .getOrElse {
                throw NoFoundException()
            }
            .filter {
                it.toString().contains(taskId)
            }.takeIf {
                it.isNotEmpty()
            } ?: throw NoFoundException()
    }
}
