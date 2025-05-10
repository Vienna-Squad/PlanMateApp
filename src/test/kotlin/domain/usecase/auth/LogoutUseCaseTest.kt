package domain.usecase.auth


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.auth.LogoutUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class LogoutUseCaseTest {

    private val  usersRepository: UsersRepository = mockk(relaxed = true)
    val logoutUseCase = LogoutUseCase(usersRepository)


    @Test
    fun `should throw any exception data when clearUserDate throw any exception`() {
        // when
        every { usersRepository.clearUserData() } throws Exception()
        //then
        assertThrows<Exception> {
            logoutUseCase.invoke()
        }
    }

    @Test
    fun `should clear user data when user logged out`() {
        // when
        logoutUseCase.invoke()
        //then
        verify { usersRepository.clearUserData() }
    }
}