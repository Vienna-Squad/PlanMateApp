package domain.usecase.auth


import io.mockk.every
import io.mockk.mockk
import org.example.domain.NotFoundException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.usecase.auth.LogoutUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LogoutUseCaseTest {

    private val authenticationRepository: AuthenticationRepository = mockk()
    private val logoutUseCase = LogoutUseCase(authenticationRepository)

    @Test
    fun `invoke should return success when current user exists and logout succeeds`() {
        // given
        every { authenticationRepository.getCurrentUser() } returns Result.success(
            User(username = "ahmed", hashedPassword = "password", type = UserType.ADMIN)
        )
        every { authenticationRepository.logout() } returns Result.success(Unit)

        // when
        val result = logoutUseCase.invoke()

        // then
        assertEquals(Result.success(Unit), result)
    }

    @Test
    fun `invoke should throw NoFoundException when current user is not found`() {
        // given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(NotFoundException(""))

        // when & then
        assertThrows<NotFoundException> {
            logoutUseCase.invoke()
        }
    }

    @Test
    fun `invoke should throw NoFoundException when logout fails`() {
        // given
        every { authenticationRepository.getCurrentUser() } returns Result.success(
            User(username = "ahmed", hashedPassword = "password", type= UserType.ADMIN)
        )
        every { authenticationRepository.logout() } returns Result.failure(NotFoundException(""))

        // when & then
        assertThrows<NotFoundException> {
            logoutUseCase.invoke()
        }
    }
}
