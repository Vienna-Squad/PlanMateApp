package org.example.domain.usecase.auth

import org.example.data.repository.UsersRepositoryImpl.Companion.encryptPassword
import org.example.domain.entity.User
import org.example.domain.entity.User.UserRole
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator

class CreateUserUseCase(
    private val usersRepository: UsersRepository,
    private val logsRepository: LogsRepository,
    private val validator: Validator
) {
    operator fun invoke(username: String, password: String, role: UserRole) {
        val currentUser = usersRepository.getCurrentUser()
        validator.canCreateUser(currentUser)
        val newUser = User(username = username, hashedPassword = encryptPassword(password), role = role)
        usersRepository.createUser(newUser)
        logsRepository.addLog(CreatedLog(
                username = currentUser.username,
                affectedId = newUser.id,
                affectedName = newUser.username,
                affectedType = Log.AffectedType.MATE
            ))
    }
}