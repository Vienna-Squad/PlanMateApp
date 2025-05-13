package org.example.domain.usecase.auth


import data.datasource.preferences.Preferences
import org.example.common.StorageType
import org.example.data.repository.UsersRepositoryImpl
import org.example.domain.AuthenticationException
import org.example.domain.repository.UsersRepository

class LoginUseCase(
    private val usersRepository: UsersRepository,
    private val preferences: Preferences = Preferences
) {
    operator fun invoke(username: String, password: String) =
        usersRepository.getAllUsers()
            .find { it.username == username && it.hashedPassword == UsersRepositoryImpl.encryptPassword(password) }
            ?.let { user ->
                usersRepository.storeCurrentUserId(user.id)
            } ?: throw AuthenticationException()

    fun getCurrentUserIfLoggedIn() = usersRepository.getCurrentUser()

    fun setStorageType(storageType: StorageType) = preferences.saveDataSourceType(storageType)
}