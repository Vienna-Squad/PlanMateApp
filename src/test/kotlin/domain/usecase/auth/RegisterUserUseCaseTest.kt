package domain.usecase.auth

import io.mockk.every
import io.mockk.mockk
import org.example.domain.NoFoundException
import org.example.domain.RegisterException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.usecase.auth.LoginUseCase
import org.example.domain.usecase.auth.RegisterUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class RegisterUserUseCaseTest {

    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    lateinit var registerUserUseCase: RegisterUserUseCase

    @BeforeEach
    fun setUp() {
        registerUserUseCase = RegisterUserUseCase(authenticationRepository)
    }

    @Test
    fun `invoke should throw RegisterException when username and password is not valid`() {
        // given
        val user = User(
            username = "  Ahm ed  ",
            password = "1234",
            type = UserType.MATE
        )
        // when & then
        assertThrows<RegisterException> {
            registerUserUseCase.invoke(user.username, user.password, user.type)
        }
    }

    @Test
    fun `invoke should throw RegisterException when the result of getAllUsers list is failure from authenticationRepository`() {
        // given
        val user = User(
            username = "AhmedNaser7",
            password = "12345678",
            type = UserType.MATE
        )
        every { authenticationRepository.getAllUsers() } returns Result.failure(RegisterException())

        // when&then
        assertThrows<RegisterException> {
            registerUserUseCase.invoke(user.username, user.password, user.type)
        }
    }

    @Test
    fun `invoke should throw RegisterException when the user found in getAllUsers list given the result of getAllUsers is success`() {
        // given
        val user = User(
            username = "AhmedNaser",
            password = "12345678",
            type = UserType.MATE
        )
        every { authenticationRepository.getAllUsers() } returns Result.success(
            listOf(
                User(
                    username = "AhmedNaser",
                    password = "245G546dfgdfg5",
                    type = UserType.MATE
                ),
                User(
                    username = "Marmosh",
                    password = "245Gfdksfm653",
                    type = UserType.MATE
                )
            )
        )
        // when&then
        assertThrows<NoFoundException> {
            registerUserUseCase.invoke(user.username, user.password, user.type)
        }
    }

    @Test
    fun `invoke should throw RegisterException when create user of authenticationRepository return failure`() {
        // given
        val user = User(
            username = "AhmedNaser7",
            password = "12345678",
            type = UserType.MATE
        )
        every { authenticationRepository.getAllUsers() } returns Result.success(
            listOf(
                User(
                    username = "MohamedSalah",
                    password = "245G546dfgdfg5",
                    type = UserType.MATE
                ),
                User(
                    username = "Marmosh",
                    password = "245Gfdksfm653",
                    type = UserType.MATE
                )
            )
        )
        every {  authenticationRepository.createUser(any())} returns Result.failure(RegisterException())

        // when&then
        assertThrows<RegisterException> {
            registerUserUseCase.invoke(user.username, user.password, user.type)
        }
    }

    @Test
    fun `invoke should complete registration when all validation and methods is success `() {
        // given
        val user = User(
            username = "AhmedNaser7",
            password = "12345678",
            type = UserType.MATE
        )
        every { authenticationRepository.getAllUsers() } returns Result.success(
            listOf(
                User(
                    username = "MohamedSalah",
                    password = "245G546dfgdfg5",
                    type = UserType.MATE
                ),
                User(
                    username = "Marmosh",
                    password = "245Gfdksfm653",
                    type = UserType.MATE
                )
            )
        )
        authenticationRepository.createUser(user).isSuccess

        // when&then
        registerUserUseCase.invoke(user.username, user.password, user.type)
    }

}