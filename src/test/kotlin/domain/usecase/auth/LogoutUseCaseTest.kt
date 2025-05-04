package domain.usecase.auth


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NotFoundException
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import org.example.domain.usecase.auth.LogoutUseCase
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LogoutUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var logoutUseCase: LogoutUseCase

    @BeforeEach
    fun setUp() {
        authRepository = mockk(relaxed = true)
        logoutUseCase = LogoutUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when current user exists and logout succeeds`() {
        // given
        every { authRepository.getCurrentUser() } returns Result.success(
            User(username = "ahmed", hashedPassword = "password", role = UserRole.ADMIN)
        )
        every { authRepository.logout() } returns Result.success(Unit)

        // when & then
        assertDoesNotThrow {
            logoutUseCase.invoke()
        }
        verify { authRepository.logout() }
    }


    @Test
    fun `invoke should throw NoFoundException when logout fails`() {
        // given
        every { authRepository.getCurrentUser() } returns Result.success(
            User(username = "ahmed", hashedPassword = "password", role = UserRole.ADMIN)
        )
        every { authRepository.logout() } returns Result.failure(NotFoundException(""))

        // when & then
        assertThrows<NotFoundException> {
            logoutUseCase.invoke()
        }
    }
}