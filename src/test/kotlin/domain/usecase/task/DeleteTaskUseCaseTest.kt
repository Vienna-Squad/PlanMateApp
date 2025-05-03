//package domain.usecase.task
//
//import io.mockk.*
//import org.example.domain.*
//import org.example.domain.entity.*
//import org.example.domain.repository.*
//import org.example.domain.usecase.task.DeleteTaskUseCase
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//import java.time.LocalDateTime
//import java.util.*
//
//class DeleteTaskUseCaseTest {
//
//    private lateinit var projectsRepository: ProjectsRepository
//    private lateinit var tasksRepository: TasksRepository
//    private lateinit var logsRepository: LogsRepository
//    private lateinit var authenticationRepository: AuthenticationRepository
//
//    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
//    private val user = User(
//        id = UUID.randomUUID(),
//        username = "adminUser",
//        hashedPassword = "hashed",
//        type = UserType.ADMIN,
//        cratedAt = LocalDateTime.now()
//    )
//
//    private val mateUser = user.copy(
//        id = UUID.randomUUID(),
//        username = "mateUser",
//        type = UserType.MATE
//    )
//
//    private val fixedProjectId = UUID.fromString("9f1602cc-87c0-4319-96b5-5d43766b9ae9") // consistent across test
//
//    private val project = Project(
//        id = fixedProjectId,
//        name = "Project A",
//        states = listOf("todo", "done"),
//        createdBy = user.id,
//        cratedAt = LocalDateTime.now(),
//        matesIds = listOf()
//    )
//
//    private val task = Task(
//        id = UUID.randomUUID(),
//        title = "Task A",
//        state = "todo",
//        assignedTo = listOf(),
//        createdBy = user.id,
//        createdAt = LocalDateTime.now(),
//        projectId = fixedProjectId // matches project.id exactly
//    )
//
//
//    @BeforeEach
//    fun setUp() {
//        projectsRepository = mockk()
//        tasksRepository = mockk()
//        logsRepository = mockk()
//        authenticationRepository = mockk()
//        deleteTaskUseCase = DeleteTaskUseCase(
//            projectsRepository,
//            tasksRepository,
//            logsRepository,
//            authenticationRepository
//        )
//    }
//
//    @Test
//    fun `should delete task and log when authorized admin deletes own project task`() {
//        // Given
//        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
//        every { projectsRepository.getProjectById(project.id) } returns Result.success(project) // fixed method name
//        every { tasksRepository.getTaskById(task.id) } returns Result.success(task)
//        every { tasksRepository.deleteTaskById(task.id) } returns Result.success(Unit)
//        every {
//            logsRepository.addLog(match {
//                it.username == user.username &&
//                        it.affectedId == task.id &&
//                        it.affectedType == Log.AffectedType.TASK
//            })
//        } returns Result.success(Unit)
//
//        // When
//        deleteTaskUseCase(task.id)
//
//        // Then
//        verify { tasksRepository.deleteTaskById(task.id) }
//        verify {
//            logsRepository.addLog(match {
//                it.username == user.username &&
//                        it.affectedId == task.id &&
//                        it.affectedType == Log.AffectedType.TASK
//            })
//        }
//    }
//
//
////
////    @Test
////    fun `should throw UnauthorizedException if no user is authenticated`() {
////        every { authenticationRepository.getCurrentUser() } returns Result.failure(Throwable())
////
////        assertThrows<UnauthorizedException> {
////            deleteTaskUseCase.invoke(task.id)
////        }
////
////        verify(exactly = 0) { tasksRepository.delete(any()) }
////        verify(exactly = 0) { logsRepository.add(any()) }
////    }
////
////    @Test
////    fun `should throw AccessDeniedException if user is MATE`() {
////        every { authenticationRepository.getCurrentUser() } returns Result.success(mateUser)
////
////        assertThrows<AccessDeniedException> {
////            deleteTaskUseCase.invoke(task.id)
////        }
////
////        verify(exactly = 0) { projectsRepository.get(any()) }
////        verify(exactly = 0) { tasksRepository.delete(any()) }
////    }
////
////    @Test
////    fun `should throw NoFoundException if project does not exist`() {
////        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
////        every { projectsRepository.get(task.id) } returns Result.failure(Throwable())
////
////        assertThrows<NoFoundException> {
////            deleteTaskUseCase.invoke(task.id)
////        }
////
////        verify(exactly = 0) { tasksRepository.get(any()) }
////        verify(exactly = 0) { tasksRepository.delete(any()) }
////    }
////
////    @Test
////    fun `should throw AccessDeniedException if user did not create the project`() {
////        val otherProject = project.copy(createdBy = "otherUser")
////        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
////        every { projectsRepository.get(task.id) } returns Result.success(otherProject)
////
////        assertThrows<AccessDeniedException> {
////            deleteTaskUseCase.invoke(task.id)
////        }
////
////        verify(exactly = 0) { tasksRepository.get(any()) }
////        verify(exactly = 0) { tasksRepository.delete(any()) }
////    }
////
////    @Test
////    fun `should throw NoFoundException if task does not exist`() {
////        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
////        every { projectsRepository.get(task.id) } returns Result.success(project)
////        every { tasksRepository.get(task.id) } returns Result.failure(Throwable())
////
////        assertThrows<NoFoundException> {
////            deleteTaskUseCase.invoke(task.id)
////        }
////
////        verify(exactly = 0) { tasksRepository.delete(any()) }
////    }
////
////
////
////    @Test
////    fun `should throw AccessDeniedException if task projectId does not match project id`() {
////        val mismatchedTask = task.copy(projectId = "otherProjectId")
////        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
////        every { projectsRepository.get(task.id) } returns Result.success(project)
////        every { tasksRepository.get(task.id) } returns Result.success(mismatchedTask)
////
////        assertThrows<AccessDeniedException> {
////            deleteTaskUseCase.invoke(task.id)
////        }
////
////        verify(exactly = 0) { tasksRepository.delete(any()) }
////    }
////}
//    }
