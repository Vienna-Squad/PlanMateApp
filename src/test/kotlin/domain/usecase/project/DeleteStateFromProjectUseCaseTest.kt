//package domain.usecase.project
//
//import domain.usecase.project.DeleteStateFromProjectUseCase
//import io.mockk.every
//import io.mockk.mockk
//import kotlin.test.assertTrue
//import kotlin.test.assertFalse
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class DeleteStateFromProjectUseCaseTest {
//
//    private lateinit var statesRepository: StatesRepository
//    private lateinit var deleteStateFromProjectUseCase: DeleteStateFromProjectUseCase
//
//    private val projectId = "project123"
//
//    @BeforeEach
//    fun setUp() {
//        statesRepository = mockk()
//        deleteStateFromProjectUseCase = DeleteStateFromProjectUseCase(statesRepository)
//    }
//
//    @Test
//    fun `should return true when deletion is successful`() {
//        // given
//        val state = "active"
//        every { statesRepository.deleteStateFromProject(projectId, state) } returns true
//
//        // when
//        val result = deleteStateFromProjectUseCase(projectId, state)
//
//        // then
//        assertTrue(result)
//    }
//
//    @Test
//    fun `should return false when deletion fails`() {
//        // given
//        val state = "active"
//        every { statesRepository.deleteStateFromProject(projectId, state) } returns false
//
//        // when
//        val result = deleteStateFromProjectUseCase(projectId, state)
//
//        // then
//        assertFalse(result)
//    }
//
//    @Test
//    fun `should return false when state does not exist`() {
//        // given
//        val state = "nonexistent"
//        every { statesRepository.deleteStateFromProject(projectId, state) } returns false
//
//        // when
//        val result =  deleteStateFromProjectUseCase(projectId, state)
//
//        // then
//        assertFalse(result)
//    }
//
//}
