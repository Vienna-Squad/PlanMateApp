package org.example.domain.repository

import org.example.domain.entity.User

interface AuthenticationRepository {
    fun getUsers(): Result<List<User>>
    fun addUser(user: User): Boolean
}