package domain.usecase.auth

import io.mockk.every
import io.mockk.mockk
import org.example.domain.LoginException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.usecase.auth.LoginUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import kotlin.test.Test
import kotlin.test.assertTrue

class LoginUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    lateinit var loginUseCase: LoginUseCase
    @BeforeEach
    fun setUp() {
        loginUseCase = LoginUseCase(authenticationRepository)
    }

    // red
    // green

    @Test
    fun `invoke should return result of failure with LoginException when the user is not found in storage`(){
        // given
        val users = listOf(User(
            username = "Ahmed",
            password = "12345678",
            type = UserType.MATE
        ))
        every { authenticationRepository.getUsers()} returns Result.success(users)

        // when
        val result = loginUseCase.invoke("Medo","23333")

        // then
        assertTrue { result.isFailure }
    }

    @Disabled
    @Test
    fun `invoke should return result of failure with LoginException when the result of getUsers is Failure given user name and password is not correct`(){
        // given
        val users = listOf(User(
            username = "Ahmed",
            password = "12345678",
            type = UserType.MATE
        ))
        every { authenticationRepository.getUsers()} returns Result.failure(LoginException())

        // when
        val result = loginUseCase.invoke("Medo","23333")

        // then
        assertTrue { result.isFailure}
    }
    @Disabled
    @Test
    fun `invoke should return result of failure with LoginException when the result of getUsers is Failure given user name and password is correct`(){
        // given
        val users = listOf(User(
            username = "Ahmed",
            password = "12345678",
            type = UserType.MATE
        ))
        every { authenticationRepository.getUsers()} returns Result.success(users)

        // when
        val result = loginUseCase.invoke("Ahmed","12345678")

        // then
        assertTrue { result.isFailure }
    }

    @Disabled
    @Test
    fun `invoke should return result of Success with user model when the user is found in storage`(){
        // given
        val users = listOf(User(
            username = "Ahmed",
            password = "12345678",
            type = UserType.MATE
        ))
        every { authenticationRepository.getUsers()} returns Result.success(users)

        // when
        val result = loginUseCase.invoke("Ahmed","12345678")

        // then
        assertTrue { result.isSuccess }
    }


}