package domain.usecase.auth


import io.mockk.every
import io.mockk.mockk
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.auth.LogoutUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class LogoutUseCaseTest {

    private val  usersRepository: UsersRepository = mockk(relaxed = true)
    val logoutUseCase = LogoutUseCase(usersRepository)


    @Test
    fun `should clear user data when user logged out`() {
        // given
        every { usersRepository.clearUserData()} returns Unit

        // when&then
        logoutUseCase.invoke()
    }
}