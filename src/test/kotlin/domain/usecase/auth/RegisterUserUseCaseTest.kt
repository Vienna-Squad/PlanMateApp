package domain.usecase.auth

import io.mockk.every
import io.mockk.mockk
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
    fun `invoke should throw RegisterException if username and password is not valid`() {
        // given
        val user = User(
            username = "Ahmed",
            password = "1234",
            type = UserType.MATE
        )

        assertThrows<RegisterException> {
            registerUserUseCase.invoke(user.username, user.password, user.type)
        }


    }


}