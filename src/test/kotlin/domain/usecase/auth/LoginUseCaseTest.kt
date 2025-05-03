package domain.usecase.auth

import io.mockk.every
import io.mockk.mockk
import org.example.domain.LoginException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.usecase.auth.LoginUseCase
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertTrue

class LoginUseCaseTest {
    companion object {
        private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
        lateinit var loginUseCase: LoginUseCase

        @BeforeAll
        @JvmStatic
        fun setUp() {
            loginUseCase = LoginUseCase(authenticationRepository)
        }
    }

    @Test
    fun `invoke should return result of failure of LoginException when the user is not found in data`() {
        // given
        every { authenticationRepository.login(any(), any()) } returns Result.failure(LoginException(""))
        // when


        // then
        assertThrows<LoginException> {
            loginUseCase.invoke(username = "Medo", password = "235657333")
        }
    }


//    @Test
//    fun `invoke should return result of Success with user model when the user is found in storage`(){
//        // given
//        every { authenticationRepository.login(any(),any()) } returns Result.success(User(
//            username = "ahmed",
//            hashedPassword = "8345bfbdsui",
//            type = UserType.MATE,
//        ))
//        // when
//        val result = loginUseCase.invoke("Medo","235657333")
//
//        // then
//        assertTrue { result. }
//    }


}
