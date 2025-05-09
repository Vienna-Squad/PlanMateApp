package domain.usecase.auth

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.entity.User
import org.example.domain.entity.User.UserRole
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.auth.CreateUserUseCase
import kotlin.test.Test

class CreateUserUseCaseTest {

    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)

    val createUserUseCase = CreateUserUseCase(usersRepository, logsRepository)


    @Test
    fun `should create new user when user complete register with valid username and password`() {
        // given
        val user = User(
            username = " Ah med ",
            hashedPassword = "123456789",
            role = UserRole.MATE
        )
        every { usersRepository.createUser(any()) } returns Unit
        // when & then
        createUserUseCase.invoke(user.username, user.hashedPassword, user.role)
    }

    @Test
    fun `should add log for new user when user complete register with valid username and password`() {
        // given
        val user = User(
            username = " Ah med ",
            hashedPassword = "123456789",
            role = UserRole.MATE
        )
        // when
        createUserUseCase.invoke(user.username, user.hashedPassword, user.role)
        //then
        verify { logsRepository.addLog(any()) }
    }


}