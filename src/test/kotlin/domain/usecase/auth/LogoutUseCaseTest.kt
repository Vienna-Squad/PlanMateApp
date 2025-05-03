package domain.usecase.auth


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NotFoundException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.usecase.auth.LogoutUseCase
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LogoutUseCaseTest {

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var logoutUseCase: LogoutUseCase

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        logoutUseCase = LogoutUseCase(authenticationRepository)
    }

    @Test
    fun `invoke should return success when current user exists and logout succeeds`() {
        // given
        every { authenticationRepository.getCurrentUser() } returns Result.success(
            User(username = "ahmed", hashedPassword = "password", type = UserType.ADMIN)
        )
        every { authenticationRepository.logout() } returns Result.success(Unit)

        // when & then
        assertDoesNotThrow {
            logoutUseCase.invoke()
        }
        verify { authenticationRepository.logout() }
    }


    @Test
    fun `invoke should throw NoFoundException when logout fails`() {
        // given
        every { authenticationRepository.getCurrentUser() } returns Result.success(
            User(username = "ahmed", hashedPassword = "password", type = UserType.ADMIN)
        )
        every { authenticationRepository.logout() } returns Result.failure(NotFoundException(""))

        // when & then
        assertThrows<NotFoundException> {
            logoutUseCase.invoke()
        }
    }
}