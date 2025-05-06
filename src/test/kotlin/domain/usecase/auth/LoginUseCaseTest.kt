package domain.usecase.auth

import io.mockk.every
import io.mockk.mockk
import org.example.data.repository.UsersRepositoryImpl.Companion.encryptPassword
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.auth.LoginUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginUseCaseTest {
    companion object{
        private val usersRepository: UsersRepository = mockk(relaxed = true)
        lateinit var loginUseCase: LoginUseCase
        @BeforeAll
        @JvmStatic
        fun setUp() {
            loginUseCase = LoginUseCase(usersRepository)
        }
    }


    @Test
    fun `invoke should return false when users storage is empty`() {
        // given
        every { usersRepository.getAllUsers() } returns emptyList()

        // when & then
        val result = loginUseCase.invoke(username = "Ahmed", password = "12345678")
        assertFalse { result }
    }

    @Test
    fun `invoke should return false when user not in users storage`() {
        // given
        every { usersRepository.getAllUsers() } returns listOf(User(
            username = "ahmed",
            hashedPassword = encryptPassword("12345678"),
            role = UserRole.MATE,
        ))

        // when & then
        val result = loginUseCase.invoke(username = "Mohamed Magdy", password = "1345433")
        assertFalse { result }
    }


    @Test
    fun `invoke should return true when the user is found in storage`() {
        // given
        every { usersRepository.getAllUsers() } returns listOf(User(
            username = "ahmed",
            hashedPassword = encryptPassword("12345678"),
            role = UserRole.MATE,
        ))

        // when
        val result = loginUseCase.invoke("ahmed", "12345678")

        // then
        assertTrue { result }
    }

    @Test
    fun `getCurrentUserIfLoggedIn invoke should return user when user is logged in`() {
        // given
        val loggedUser = User(
            username = "ahmed",
            hashedPassword = encryptPassword("12345678"),
            role = UserRole.MATE,
        )
        every { usersRepository.getCurrentUser() } returns User(
            username = "ahmed",
            hashedPassword = encryptPassword("12345678"),
            role = UserRole.MATE,
        )

        // when
        val currentUser = loginUseCase.getCurrentUserIfLoggedIn()

        //then
        assertEquals(loggedUser.username,currentUser?.username)
        assertEquals(loggedUser.hashedPassword,currentUser?.hashedPassword)
    }





}