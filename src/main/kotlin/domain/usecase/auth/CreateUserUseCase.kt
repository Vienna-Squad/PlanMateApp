package org.example.domain.usecase.auth

import org.example.domain.entity.CreatedLog
import org.example.domain.entity.Log
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.UsersRepository

class CreateUserUseCase(
    private val usersRepository: UsersRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(username: String, password: String, role: UserRole) =
        User(username = username, hashedPassword = password, role = role).let { newUser ->
            usersRepository.createUser(newUser)
            logsRepository.addLog(
                CreatedLog(
                    username = usersRepository.getCurrentUser().username,
                    affectedId = newUser.id.toString(),
                    affectedType = Log.AffectedType.MATE
                )
            )
        }
}