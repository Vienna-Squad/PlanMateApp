package org.example.domain.usecase.auth

import org.example.domain.AccessDeniedException
import org.example.domain.entity.User
import org.example.domain.entity.User.UserRole
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.UsersRepository

class CreateUserUseCase(
    private val usersRepository: UsersRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(username: String, password: String, role: UserRole) =
        usersRepository.getCurrentUser().let { currentUser ->
            if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException("feature")
            User(username = username, hashedPassword = password, role = role).let { newUser ->
                usersRepository.createUser(newUser)
                logsRepository.addLog(
                    CreatedLog(
                        username = currentUser.username,
                        affectedId = newUser.id,
                        affectedName = newUser.username,
                        affectedType = Log.AffectedType.MATE
                    )
                )
            }
        }
}