package data.storage.repository

import data.storage.UserCsvStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.storage.repository.AuthenticationCsvRepository
import org.example.domain.NoFoundException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.security.MessageDigest
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthenticationCsvRepositoryTest {
    private lateinit var repository: AuthenticationCsvRepository
    private lateinit var storage: UserCsvStorage

    private val user = User(
        id = "U1",
        username = "user1",
        password = "pass1",
        type = UserType.ADMIN,
        cratedAt = LocalDateTime.now()
    )

    private val anotherUser = User(
        id = "U2",
        username = "user2",
        password = "pass2",
        type = UserType.MATE,
        cratedAt = LocalDateTime.now()
    )

    @BeforeEach
    fun setup() {
        storage = mockk(relaxed = true)
        repository = AuthenticationCsvRepository(storage)
    }

    @Test
    fun `should return list of users when getAllUsers is called`() {
        // Given
        every { storage.read() } returns listOf(user, anotherUser)

        // When
        val result = repository.getAllUsers()

        // Then
        assertEquals(listOf(user, anotherUser), result.getOrThrow())
    }

    @Test
    fun `should return failure when getAllUsers fails`() {
        // Given
        every { storage.read() } throws NoFoundException()

        // When
        val result = repository.getAllUsers()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should create user successfully  when createUser is called`() {
        // Given
        every { storage.read() } returns emptyList()
        val expectedUser = user.copy(password = user.password.toMD5())

        // When
         repository.createUser(user)

        // Then
        verify { storage.append(expectedUser) }
    }

    @Test
    fun `should return failure when createUser is called with existing user`() {
        // Given
        every { storage.read() } returns listOf(user)

        // When
        val result = repository.createUser(user)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when createUser fails`() {
        // Given
        every { storage.read() } returns emptyList()
        every { storage.append(any()) } throws NoFoundException()

        // When
        val result = repository.createUser(user)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should update current user when creating a new user`() {
        // Given
        every { storage.read() } returns emptyList()
        repository.createUser(user)
        every { storage.read() } returns listOf(user.copy(password = user.password.toMD5()))
        val expectedAnotherUser = anotherUser.copy(password = anotherUser.password.toMD5())
        repository.createUser(anotherUser)
        every { storage.read() } returns listOf(user.copy(password = user.password.toMD5()), expectedAnotherUser)
        // When
        val currentUserResult = repository.getCurrentUser()

        // Then
        assertTrue(currentUserResult.isSuccess)
    }

    @Test
    fun `should return current user when getCurrentUser is called`() {
        // Given
        every { storage.read() } returns emptyList()
        val expectedUser = user.copy(password = user.password.toMD5())
        repository.createUser(user)
        every { storage.read() } returns listOf(expectedUser)
        // When
        val result = repository.getCurrentUser()

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should return failure when getCurrentUser fails with no current user set`() {
        // Given
        every { storage.read() } returns listOf(user)

        // When
        val result = repository.getCurrentUser()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when getCurrentUser fails to read`() {
        // Given
        every { storage.read() } returns emptyList()
        repository.createUser(user)
        every { storage.read() } throws NoFoundException()

        // When
        val result = repository.getCurrentUser()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when getCurrentUser fails to find user`() {
        // Given
        every { storage.read() } returns emptyList()
        repository.createUser(user)
        every { storage.read() } returns emptyList()

        // Then
        val result = repository.getCurrentUser()

        // Then
        assertTrue(result.isFailure)

    }

    @Test
    fun `should return user when getUser is called with valid id from multiple users`() {
        // Given
        every { storage.read() } returns listOf(user, anotherUser)

        // When
        val result = repository.getUser("U2")

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should return failure when getUser is called with invalid id`() {
        // Given
        every { storage.read() } returns listOf(user, anotherUser)

        // When
        val result = repository.getUser("invalid_id")

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when getUser fails to read`() {
        // Given
        every { storage.read() } throws NoFoundException()

        // When
        val result = repository.getUser("U1")

        // Then
        assertTrue(result.isFailure)
    }

  
    private fun String.toMD5(): String {
        val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}