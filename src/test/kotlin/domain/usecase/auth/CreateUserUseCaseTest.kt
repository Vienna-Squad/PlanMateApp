package domain.usecase.auth

import io.mockk.every
import io.mockk.mockk
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.auth.CreateUserUseCase
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class CreateUserUseCaseTest {

    private val usersRepository: UsersRepository = mockk(relaxed = true)
    lateinit var createUserUseCase: CreateUserUseCase

    @BeforeEach
    fun setUp() {
        createUserUseCase = CreateUserUseCase(usersRepository)
    }


    @Test
    fun `invoke should create new user when user complete register with valid username and password`() {
        // given
        val user = User(
            username = " Ah med ",
            hashedPassword = "123456789",
            role = UserRole.MATE
        )
        every { usersRepository.createUser(any()) } returns Unit
        // when & then
        createUserUseCase.invoke(user.username,user.hashedPassword, user.role)
    }



}