package domain.usecase.auth

import io.mockk.every
import io.mockk.mockk
import org.example.domain.LoginException
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import org.example.domain.usecase.auth.LoginUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class LoginUseCaseTest {
    companion object{
        private val authRepository: AuthRepository = mockk(relaxed = true)
        lateinit var loginUseCase: LoginUseCase
        @BeforeAll
        @JvmStatic
        fun setUp() {
            loginUseCase = LoginUseCase(authRepository)
        }
    }


    @Test
    fun `invoke should throw LoginException when the user is not found in data`() {
        // given
        every { authRepository.login(any(), any()) } returns Result.failure(LoginException(""))

        // when & then
        assertThrows<LoginException> {
            loginUseCase.invoke(username = "Medo", password = "235657333")
        }
    }


    @Test
    fun `invoke should return user model when the user is found in storage`() {
        // given
        val expectedUser = User(
            username = "ahmed",
            hashedPassword = "8345bfbdsui",
            role = UserRole.MATE,
        )
        every { authRepository.login(any(), any()) } returns Result.success(expectedUser)

        // when
        val result = loginUseCase.invoke("Medo", "235657333")

        // then
        assertEquals(expectedUser, result)
    }



}