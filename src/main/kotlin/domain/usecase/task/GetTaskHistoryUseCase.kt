package org.example.domain.usecase.task

import org.example.domain.NotFoundException
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
          throw UnauthorizedException(
                "You are not authorized to perform this action. Please log in again."
          )
        }
        return logsRepository.getAllLogs()
            .getOrElse {
                throw NotFoundException(
                    "No logs found. Please check the task ID and try again."
                )
            }
            .filter {
                it.toString().contains(taskId)
            }.takeIf {
                it.isNotEmpty()
            } ?: throw NotFoundException(
                "No logs found for task ID $taskId. Please check the task ID and try again."
            )
    }
}
