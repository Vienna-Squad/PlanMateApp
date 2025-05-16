package domain.usecase.auth

import dummyAdmin
import dummyMate
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.exceptions.FeatureAccessDeniedException
import org.example.domain.entity.User
import org.example.domain.entity.User.UserRole
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import org.example.domain.usecase.auth.CreateUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CreateUserUseCaseTest {

    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)

    lateinit var createUserUseCase: CreateUserUseCase

    @BeforeEach
    fun setUp() {
        createUserUseCase = CreateUserUseCase(usersRepository, logsRepository, Validator)

    }
    // red then green

    @Test
    fun `should throw FeatureAccessDeniedException when user is not admin`() {
        // given
        val user = User(
            username = " Ah med ",
            hashedPassword = "123456789",
            role = UserRole.MATE
        )
        every { usersRepository.getCurrentUser() } returns dummyMate
        // when & then
        assertThrows<FeatureAccessDeniedException> {
            createUserUseCase.invoke(user.username, user.hashedPassword, user.role)
        }
    }


    @Test
    fun `should create new mate when user complete register with valid username and password`() {
        // given
        val user = User(
            username = "federico valverdie",
            hashedPassword = "123456789",
            role = UserRole.MATE
        )
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        // when
        createUserUseCase.invoke(user.username, user.hashedPassword, user.role)

        //then
        verify { usersRepository.createUser(any()) }
        verify { logsRepository.addLog(any()) }
    }

    @Test
    fun `should create new admin when user complete register with valid username and password`() {
        // given
        val user = User(
            username = "my uncle luka modric",
            hashedPassword = "123456789",
            role = UserRole.ADMIN
        )
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        // when
        createUserUseCase.invoke(user.username, user.hashedPassword, user.role)

        //then
        verify { usersRepository.createUser(any()) }
        verify { logsRepository.addLog(any()) }
    }


}