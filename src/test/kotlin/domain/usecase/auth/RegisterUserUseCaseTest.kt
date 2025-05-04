package domain.usecase.auth

import io.mockk.every
import io.mockk.mockk
import org.example.domain.RegisterException
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import org.example.domain.usecase.auth.RegisterUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class RegisterUserUseCaseTest {

    private val authRepository: AuthRepository = mockk(relaxed = true)
    lateinit var registerUserUseCase: RegisterUserUseCase

    @BeforeEach
    fun setUp() {
        registerUserUseCase = RegisterUserUseCase(authRepository)
    }


    @Test
    fun `invoke should throw RegisterException when username is not valid`() {
        // given
        val user = User(
            username = " Ah med ",
            hashedPassword = "123456789",
            role = UserRole.MATE
        )
        every { authRepository.getCurrentUser() } returns Result.success(
            User(
                username = "Ahmed",
                hashedPassword = "234sdfg5hn",
                role = UserRole.ADMIN,
            )
        )
        // when & then
        assertThrows<RegisterException> {
            registerUserUseCase.invoke(user.username, user.hashedPassword, user.role)
        }
    }



    @Test
    fun `invoke should throw RegisterException when password is not valid`() {
        // given
        val user = User(
            username = "AhmedNasser",
            hashedPassword = "1234",
            role = UserRole.MATE
        )
        every { authRepository.getCurrentUser() } returns Result.success(
            User(
                username = "Ahmed",
                hashedPassword = "234sdfg5hn",
                role = UserRole.ADMIN,
            )
        )
        // when & then
        assertThrows<RegisterException> {
            registerUserUseCase.invoke(user.username, user.hashedPassword, user.role)
        }
    }
    @Test
    fun `invoke should throw RegisterException when both username and password are not valid`() {
        // given
        val user = User(
            username = " Ah med ",
            hashedPassword = "1234",
            role = UserRole.MATE
        )

        // when & then
        assertThrows<RegisterException> {
            registerUserUseCase.invoke(user.username, user.hashedPassword, user.role)
        }
    }




    @Test
    fun `invoke should throw RegisterException when create user of authenticationRepository return failure`() {
        // given
        val user = User(
            username = "AhmedNaser7",
            hashedPassword = "12345678",
            role = UserRole.MATE
        )
        every { authRepository.getCurrentUser() } returns Result.success(
            User(
                username = "Ahmed",
                hashedPassword = "234sdfg5hn",
                role = UserRole.ADMIN,
            )
        )
        every { authRepository.getAllUsers() } returns Result.success(
            listOf(
                User(
                    username = "MohamedSalah",
                    hashedPassword = "245G546dfgdfg5",
                    role = UserRole.MATE
                ),
                User(
                    username = "Marmosh",
                    hashedPassword = "245Gfdksfm653",
                    role = UserRole.MATE
                )
            )
        )
        every { authRepository.createUser(any()) } returns Result.failure(RuntimeException(""))

        // when&then
        assertThrows<RegisterException> {
            registerUserUseCase.invoke(user.username, user.hashedPassword, user.role)
        }
    }

    @Test
    fun `invoke should complete registration when all validation and methods is success `() {
        // given
        val user = User(
            username = "AhmedNaser7",
            hashedPassword = "12345678",
            role = UserRole.MATE
        )
        every { authRepository.getCurrentUser() } returns Result.success(
            User(
                username = "Ahmed",
                hashedPassword = "234sdfg5hn",
                role = UserRole.ADMIN,
            )
        )
        every { authRepository.getAllUsers() } returns Result.success(
            listOf(
                User(
                    username = "MohamedSalah",
                    hashedPassword = "245G546dfgdfg5",
                    role = UserRole.MATE
                ),
                User(
                    username = "Marmosh",
                    hashedPassword = "245Gfdksfm653",
                    role = UserRole.MATE
                )
            )
        )
        every { authRepository.createUser(any()) } returns Result.success(Unit)


        // when&then
        registerUserUseCase.invoke(user.username, user.hashedPassword, user.role)
    }

    @Test
    fun `invoke should complete registration when user is type admin `() {
        // given
        val user = User(
            username = "AhmedNaser7",
            hashedPassword = "12345678",
            role = UserRole.ADMIN
        )
        every { authRepository.getCurrentUser() } returns Result.success(
            User(
                username = "Ahmed",
                hashedPassword = "234sdfg5hn",
                role = UserRole.ADMIN,
            )
        )
        every { authRepository.getAllUsers() } returns Result.success(
            listOf(
                User(
                    username = "MohamedSalah",
                    hashedPassword = "245G546dfgdfg5",
                    role = UserRole.MATE
                ),
                User(
                    username = "Marmosh",
                    hashedPassword = "245Gfdksfm653",
                    role = UserRole.MATE
                )
            )
        )
        every { authRepository.createUser(any()) } returns Result.success(Unit)


        // when&then
        registerUserUseCase.invoke(user.username, user.hashedPassword, user.role)
    }


}