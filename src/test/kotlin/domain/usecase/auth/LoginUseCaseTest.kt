package domain.usecase.auth

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.repository.UsersRepositoryImpl.Companion.encryptPassword
import org.example.domain.UnauthorizedException
import org.example.domain.entity.User
import org.example.domain.entity.User.UserRole
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.auth.LoginUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test


class LoginUseCaseTest {

    private val usersRepository: UsersRepository = mockk(relaxed = true)
    lateinit var loginUseCase: LoginUseCase

    @BeforeEach
    fun setUp() {
        loginUseCase = LoginUseCase(usersRepository)
    }


    @Test
    fun `invoke should throw UnauthorizedException when list of users is empty`() {
        // given
        every { usersRepository.getAllUsers() } returns emptyList()

        // when & then
        assertThrows<UnauthorizedException> {
            loginUseCase.invoke(username = "Ahmed", password = "12345678")
        }
    }


    @Test
    fun `invoke should throw UnauthorizedException when user not correct`() {
        // given
        every { usersRepository.getAllUsers() } returns listOf(
            User(
                username = "harry kane",
                hashedPassword = encryptPassword("12345678"),
                role = UserRole.MATE,
            )
        )

        // when & then
        assertThrows<UnauthorizedException> {
            loginUseCase.invoke(username = "Ahmed", password = "12345678")
        }
    }

    @Test
    fun `invoke should throw UnauthorizedException when password not correct`() {
        // given
        every { usersRepository.getAllUsers() } returns listOf(
            User(
                username = "Ahmed",
                hashedPassword = "uofah83r",
                role = UserRole.MATE,
            )
        )

        // when & then
        assertThrows<UnauthorizedException> {
            loginUseCase.invoke(username = "Ahmed", password = "12345678")
        }
    }

    @Test
    fun `invoke should logged in when user found `() {
        // given
        every { usersRepository.getAllUsers() } returns listOf(
            User(
                username = "Ahmed",
                hashedPassword = encryptPassword("12345678"),
                role = UserRole.MATE,
            )
        )

        loginUseCase.invoke(username = "Ahmed", password = "12345678")

    }


    @Test
    fun `invoke should store user data for authorization `() {
        // given
        every { usersRepository.getAllUsers() } returns listOf(
            User(
                username = "Ahmed",
                hashedPassword = encryptPassword("12345678"),
                role = UserRole.MATE,
            )
        )

        loginUseCase.invoke(username = "Ahmed", password = "12345678")

        verify { usersRepository.storeUserData(any(), any(), any()) }

    }

    @Test
    fun `getCurrentUserIfLoggedIn should get current user when he already logged in`() {
        // given
        every { usersRepository.getAllUsers() } returns listOf(
            User(
                username = "Ahmed",
                hashedPassword = encryptPassword("12345678"),
                role = UserRole.MATE,
            )
        )

        loginUseCase.getCurrentUserIfLoggedIn()

        verify { usersRepository.getCurrentUser() }

    }

    @Test
    fun `getCurrentUserIfLoggedIn should return user when user already logged in `() {
        // given
        val user = User(
            username = "Ahmed",
            hashedPassword = encryptPassword("12345678"),
            role = UserRole.ADMIN,
        )
        every { usersRepository.getAllUsers() } returns listOf(user)
        every { usersRepository.getCurrentUser() } returns user

        //when
        val result = loginUseCase.getCurrentUserIfLoggedIn()

        //then
        assertEquals(user, result)

    }


}