package presentation.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.usecase.project.EditProjectStatesUseCase
import org.example.presentation.controller.EditProjectStatesController
import org.example.presentation.utils.interactor.Interactor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EditProjectStatesControllerTest {
    private lateinit var editProjectStatesUseCase: EditProjectStatesUseCase
    private lateinit var interactor: Interactor<String>
    private lateinit var controller: EditProjectStatesController

    @BeforeEach
    fun setUp() {
        editProjectStatesUseCase = mockk(relaxed = true)
        interactor = mockk(relaxed = true)
        controller = EditProjectStatesController(editProjectStatesUseCase, interactor)
    }

    @Test
    fun `should execute use case with correct inputs`() {
        // given
        val projectId = "123"
        val statesInput = "state1, state2"
        val expectedStates = listOf("state1", "state2")

        every { interactor.getInput() } returnsMany listOf(projectId, statesInput)

        // when
        controller.execute()

        // then
        verify { editProjectStatesUseCase(projectId, expectedStates) }
    }

    @Test
    fun `should handle exception and show error`() {
        // given
        val projectId = "123"
        val statesInput = "state1, state2"
        val exception = RuntimeException("Test exception")

        every { interactor.getInput() } returnsMany listOf(projectId, statesInput)
        every { editProjectStatesUseCase(any(), any()) } throws exception

        // when
        controller.execute()

        // then
        verify { interactor.getInput() }
        verify(exactly = 0) { editProjectStatesUseCase(projectId, any()) }
    }
}