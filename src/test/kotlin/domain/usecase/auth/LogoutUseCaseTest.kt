package domain.usecase.auth


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NotFoundException
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import org.example.domain.usecase.auth.LogoutUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class LogoutUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var logoutUseCase: LogoutUseCase

    @BeforeEach
    fun setUp() {
        authRepository = mockk(relaxed = true)
        logoutUseCase = LogoutUseCase(authRepository)
    }


    @Test
    fun `should clear user data when user logged out`() {
        // given
        every { authRepository.clearUserData()} returns Unit

        // when&then
        logoutUseCase.invoke()
    }
}