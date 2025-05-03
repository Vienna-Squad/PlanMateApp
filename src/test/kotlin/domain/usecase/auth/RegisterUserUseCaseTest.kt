//package domain.usecase.auth
//
//import io.mockk.every
//import io.mockk.mockk
//import org.example.domain.RegisterException
//import org.example.domain.entity.User
//import org.example.domain.entity.UserType
//import org.example.domain.repository.AuthenticationRepository
//import org.example.domain.usecase.auth.RegisterUserUseCase
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.assertThrows
//import kotlin.test.Test
//
//class RegisterUserUseCaseTest {
//
//    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
//    lateinit var registerUserUseCase: RegisterUserUseCase
//
//    @BeforeEach
//    fun setUp() {
//        registerUserUseCase = RegisterUserUseCase(authenticationRepository)
//    }
//
//
//    @Test
//    fun `invoke should throw RegisterException when current user not found`() {
//        // given
//        val user = User(
//            username = "Ahmed234",
//            hashedPassword = "1234234234",
//            type = UserType.MATE
//        )
//        every { authenticationRepository.getCurrentUser() } returns Result.failure(RegisterException())
//        // when & then
//        assertThrows<RegisterException> {
//            registerUserUseCase.invoke(user.username, user.hashedPassword, user.type)
//        }
//    }
//
//
//    @Test
//    fun `invoke should throw RegisterException when current user is not admin`() {
//        // given
//        val user = User(
//            username = "ahdmedf3",
//            hashedPassword = "12344234",
//            type = UserType.MATE
//        )
//        every { authenticationRepository.getCurrentUser() } returns Result.success(
//            User(
//                username = "Ahmed",
//                hashedPassword = "234sdfg5hn",
//                type = UserType.MATE,
//            )
//        )
//        // when & then
//        assertThrows<RegisterException> {
//            registerUserUseCase.invoke(user.username, user.hashedPassword, user.type)
//        }
//    }
//
//
//    @Test
//    fun `invoke should throw RegisterException when username is not valid`() {
//        // given
//        val user = User(
//            username = " Ah med ",
//            hashedPassword = "123456789",
//            type = UserType.MATE
//        )
//        every { authenticationRepository.getCurrentUser() } returns Result.success(
//            User(
//                username = "Ahmed",
//                hashedPassword = "234sdfg5hn",
//                type = UserType.ADMIN,
//            )
//        )
//        // when & then
//        assertThrows<RegisterException> {
//            registerUserUseCase.invoke(user.username, user.hashedPassword, user.type)
//        }
//    }
//
//
//    @Test
//    fun `invoke should throw RegisterException when password is not valid`() {
//        // given
//        val user = User(
//            username = "AhmedNasser",
//            hashedPassword = "1234",
//            type = UserType.MATE
//        )
//        every { authenticationRepository.getCurrentUser() } returns Result.success(
//            User(
//                username = "Ahmed",
//                hashedPassword = "234sdfg5hn",
//                type = UserType.ADMIN,
//            )
//        )
//        // when & then
//        assertThrows<RegisterException> {
//            registerUserUseCase.invoke(user.username, user.hashedPassword, user.type)
//        }
//    }
//
//
//    @Test
//    fun `invoke should throw RegisterException when the result of getAllUsers list is failure from authenticationRepository`() {
//        // given
//        val user = User(
//            username = "AhmedNaser7",
//            hashedPassword = "12345678",
//            type = UserType.MATE
//        )
//        every { authenticationRepository.getCurrentUser() } returns Result.success(
//            User(
//                username = "Ahmed",
//                hashedPassword = "234sdfg5hn",
//                type = UserType.ADMIN,
//            )
//        )
//        every { authenticationRepository.getAllUsers() } returns Result.failure(RegisterException())
//
//        // when&then
//        assertThrows<RegisterException> {
//            registerUserUseCase.invoke(user.username, user.hashedPassword, user.type)
//        }
//    }
//
//    @Test
//    fun `invoke should throw RegisterException when the user found in getAllUsers list`() {
//        // given
//        val user = User(
//            username = "AhmedNaser",
//            hashedPassword = "12345678",
//            type = UserType.MATE
//        )
//        every { authenticationRepository.getCurrentUser() } returns Result.success(
//            User(
//                username = "Ahmed",
//                hashedPassword = "234sdfg5hn",
//                type = UserType.ADMIN,
//            )
//        )
//        every { authenticationRepository.getAllUsers() } returns Result.success(
//            listOf(
//                User(
//                    username = "AhmedNaser",
//                    hashedPassword = "245G546dfgdfg5",
//                    type = UserType.MATE
//                ),
//                User(
//                    username = "Marmosh",
//                    hashedPassword = "245Gfdksfm653",
//                    type = UserType.MATE
//                )
//            )
//        )
//        // when&then
//        assertThrows<RegisterException> {
//            registerUserUseCase.invoke(user.username, user.hashedPassword, user.type)
//        }
//    }
//
//    @Test
//    fun `invoke should throw RegisterException when create user of authenticationRepository return failure`() {
//        // given
//        val user = User(
//            username = "AhmedNaser7",
//            hashedPassword = "12345678",
//            type = UserType.MATE
//        )
//        every { authenticationRepository.getCurrentUser() } returns Result.success(
//            User(
//                username = "Ahmed",
//                hashedPassword = "234sdfg5hn",
//                type = UserType.ADMIN,
//            )
//        )
//        every { authenticationRepository.getAllUsers() } returns Result.success(
//            listOf(
//                User(
//                    username = "MohamedSalah",
//                    hashedPassword = "245G546dfgdfg5",
//                    type = UserType.MATE
//                ),
//                User(
//                    username = "Marmosh",
//                    hashedPassword = "245Gfdksfm653",
//                    type = UserType.MATE
//                )
//            )
//        )
//        every { authenticationRepository.createUser(any()) } returns Result.failure(RegisterException())
//
//        // when&then
//        assertThrows<RegisterException> {
//            registerUserUseCase.invoke(user.username, user.hashedPassword, user.type)
//        }
//    }
//
//    @Test
//    fun `invoke should complete registration when all validation and methods is success `() {
//        // given
//        val user = User(
//            username = "AhmedNaser7",
//            hashedPassword = "12345678",
//            type = UserType.MATE
//        )
//        every { authenticationRepository.getCurrentUser() } returns Result.success(
//            User(
//                username = "Ahmed",
//                hashedPassword = "234sdfg5hn",
//                type = UserType.ADMIN,
//            )
//        )
//        every { authenticationRepository.getAllUsers() } returns Result.success(
//            listOf(
//                User(
//                    username = "MohamedSalah",
//                    hashedPassword = "245G546dfgdfg5",
//                    type = UserType.MATE
//                ),
//                User(
//                    username = "Marmosh",
//                    hashedPassword = "245Gfdksfm653",
//                    type = UserType.MATE
//                )
//            )
//        )
//        every { authenticationRepository.createUser(any()) } returns Result.success(Unit)
//
//
//        // when&then
//        registerUserUseCase.invoke(user.username, user.hashedPassword, user.type)
//    }
//
//    @Test
//    fun `invoke should complete registration when user is type admin `() {
//        // given
//        val user = User(
//            username = "AhmedNaser7",
//            hashedPassword = "12345678",
//            type = UserType.ADMIN
//        )
//        every { authenticationRepository.getCurrentUser() } returns Result.success(
//            User(
//                username = "Ahmed",
//                hashedPassword = "234sdfg5hn",
//                type = UserType.ADMIN,
//            )
//        )
//        every { authenticationRepository.getAllUsers() } returns Result.success(
//            listOf(
//                User(
//                    username = "MohamedSalah",
//                    hashedPassword = "245G546dfgdfg5",
//                    type = UserType.MATE
//                ),
//                User(
//                    username = "Marmosh",
//                    hashedPassword = "245Gfdksfm653",
//                    type = UserType.MATE
//                )
//            )
//        )
//        every { authenticationRepository.createUser(any()) } returns Result.success(Unit)
//
//
//        // when&then
//        registerUserUseCase.invoke(user.username, user.hashedPassword, user.type)
//    }
//
//
//}